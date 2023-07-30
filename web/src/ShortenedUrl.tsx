import { Collapse, Fade, Link, Typography } from '@mui/material';
import React from 'react';

function ShortenedUrl(props: ShortenedUrlProps) {
  return (
    <Collapse
      in={props.visible}
      timeout={1000}
      mountOnEnter
      unmountOnExit>
      <Fade
        in={props.visible}
        timeout={1000}
        mountOnEnter
        unmountOnExit>
        <Typography
          variant='h5'
          color='primary'
          style={{
            marginLeft: '50px',
            marginRight: '50px',
            marginTop: '24px',
            marginBottom: '24px',
            overflowWrap: 'anywhere'
          }}>
          <Link
            underline='none'
            href={window.location + props.text}
            target='_blank'
            rel='noopener noreferrer'>
            {window.location + props.text}
          </Link>
        </Typography>
      </Fade>
    </Collapse>
  );
}

export default ShortenedUrl;
