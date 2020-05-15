import { Checkbox, Collapse, FormControlLabel, TextField } from '@material-ui/core';
import React from 'react';

interface AliasInputProps {
  text: string;
  hasError: boolean;
  visible: boolean;
  errorText: string;
  setAlias: Function;
}

function AliasInput(props: AliasInputProps) {
  const checkBox = (
    <Checkbox
      value={props.visible}
      onChange={() => props.setAlias({ visible: !props.visible })} /> 
  );

  return (
    <>
      <div>
        <Collapse
          in={props.visible}
          timeout={500}
          mountOnEnter
          unmountOnExit>
          <TextField
            fullWidth
            error={props.hasError}
            label='Alias'
            variant='outlined'
            value={props.text}
            helperText={props.errorText}
            onChange={(e) => props.setAlias({ text: e.target.value })}
            style={{
              marginTop: '6px',
              marginBottom: '6px'
            }}
          />
        </Collapse>
      </div>
      <FormControlLabel
        control={checkBox}
        label='Use Custom Alias'
      />
    </>
  );
}

export default AliasInput;
