import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import {
  AppBar,
  Fade,
  Link,
  Toolbar,
  Typography
} from '@material-ui/core';

const useStyles = makeStyles({
  root: {
    flexGrow: 1,
  },
  title: {
    flexGrow: 1,
  },
});

export default function HomeAppBar() {
  const classes = useStyles();

  return (
    <Fade in={true} timeout={2000}>
      <div className={classes.root}>
        <AppBar color="inherit" elevation={0} position="static">
          <Toolbar>
            <Typography color="inherit" className={classes.title} variant="h5">
              <Link underline="none" href="/">
                <b>smolurl</b>
              </Link>
            </Typography>
          </Toolbar>
        </AppBar>
      </div>
    </Fade>
  );
}
