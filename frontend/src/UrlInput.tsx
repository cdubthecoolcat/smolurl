import React from 'react';
import { TextField, Button, Grid } from '@material-ui/core';

function UrlInput() {
  const [urlText, setUrlText] = React.useState('');
  const [shortText, setShortText] = React.useState('');

  const submitUrl = async (url: string) => {
    const response = await fetch('/api/urls', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        short: '',
        target: url
      })
    });
    return await response.json();
  };

  return (
    <Grid
      container
      justify="center">
      <TextField label='Url' variant='filled' value={urlText} onChange={(e) => setUrlText(e.target.value)}/>
      <Button onClick={() => submitUrl(urlText).then((data) => setShortText(data.short))}>
        Shorten
      </Button>
      {shortText.length > 0 ?
        <a href={window.location + shortText}>
          {window.location + shortText}
        </a> : null
      }
    </Grid>
  );
}

export default UrlInput;
