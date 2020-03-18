import React from 'react';
import { TextField, Button } from '@material-ui/core';

function UrlInput() {
  const [urlText, setUrlText] = React.useState('');

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
    <div>
      <TextField label='Url' variant='filled' value={urlText} onChange={(e) => setUrlText(e.target.value)}/>
      <Button onClick={() => submitUrl(urlText).then((data) => console.log(data))}>
        Shorten
      </Button>
    </div>
  );
}

export default UrlInput;
