import React, { FormEvent } from 'react';
import { TextField, Button, Grid, Typography, Link } from '@material-ui/core';

function UrlInput() {
  const [urlText, setUrlText] = React.useState<string>('');
  const [shortText, setShortText] = React.useState<string>('');

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

  const formSubmit = (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    submitUrl(urlText).then((data) => setShortText(data.short))
  }

  return (
    <Grid
      container
      spacing={0}
      direction="column"
      alignItems="center"
      justify="center"
      style={{ minHeight: '75vh' }}>
      <form onSubmit={formSubmit}>
        <TextField label='Url' variant='outlined' value={urlText} onChange={(e) => setUrlText(e.target.value)}/>
        <Button
          type="submit"
          variant="contained"
          color="primary"
          style={{
            marginLeft: '12px',
            marginRight: '12px',
            paddingTop: '16px',
            paddingBottom: '16px'
          }}
          disableElevation>Shorten</Button>
      </form>
      {shortText.length > 0 ?
        <Typography
          variant="h6"
          color="primary">
            <Link underline="none" href={window.location + shortText} target="_blank" rel="noopener noreferrer">
              {window.location + shortText}
            </Link>
        </Typography> : null
      }
    </Grid>
  );
}

export default UrlInput;
