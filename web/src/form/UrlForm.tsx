import { Button, Fade } from '@material-ui/core';
import React, { FormEvent } from 'react';
import AliasInput from './AliasInput';
import { submitUrl } from './http';
import UrlInput from './UrlInput';

enum UrlInputError {
  INVALID_URL,
  BLOCKED_DOMAIN,
  INVALID_INPUT
};

function UrlForm(props: UrlFormProps) {
  const [url, setUrl] = React.useState<UrlState>({
    text: '',
    hasError: false,
    errorMessage: ''
  });

  const [alias, setAlias] = React.useState<AliasState>({
    text: '',
    hasError: false,
    errorMessage: '',
    visible: false
  });

  const updateUrl = (updated: any) => {
    setUrl({ ...url, ...updated });
  };

  const updateAlias = (updated: any) => {
    setAlias({ ...alias, ...updated });
  };

  const updateErrors = (data: any) => {
    if (data.error) {
      let isUrlError = UrlInputError[data.error.type] !== undefined;
      updateUrl({
        hasError: isUrlError,
        errorMessage: isUrlError ? data.error.message : ''
      });
      updateAlias({
        hasError: !isUrlError,
        errorMessage: !isUrlError ? data.error.message : ''
      });
      props.setNewUrlText('')
    } else {
      const noError = {
        hasError: false,
        errorMessage: ''
      };
      updateUrl(noError);
      updateAlias(noError);
      props.setNewUrlText(data.alias);
    }
  };

  const formSubmit = (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    submitUrl({
      alias: alias.visible ? alias.text : '',
      target: url.text
    }).then(updateErrors)
  }

  return (
    <Fade
      in={true}
      timeout={2000}>
      <form onSubmit={formSubmit}>
        <UrlInput
          text={url.text}
          hasError={url.hasError}
          errorText={url.errorMessage}
          setUrl={updateUrl}
        />
        <AliasInput
          text={alias.text}
          hasError={alias.hasError}
          visible={alias.visible}
          errorText={alias.errorMessage}
          setAlias={updateAlias}
        />
        <Button
          type='submit'
          variant='contained'
          color='primary'
          disableElevation
          style={{
            marginTop: '6px',
            marginBottom: '6px'
          }}>
          Shorten
        </Button>
      </form>
    </Fade>
  );
}

export default UrlForm;
