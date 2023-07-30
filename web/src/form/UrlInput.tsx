import { TextField } from '@mui/material';
import React from 'react';

function UrlInput(props: UrlInputProps) {
  return (
    <TextField
      fullWidth
      required
      error={props.hasError}
      label='Url'
      variant='outlined'
      value={props.text}
      helperText={props.errorText}
      onChange={(e) => props.setUrl({ text: e.target.value })}
      style={{
        marginTop: '6px',
        marginBottom: '6px'
      }}
    />
  );
}

export default UrlInput;
