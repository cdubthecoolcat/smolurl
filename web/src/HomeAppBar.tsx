import { AppBar, Fade, Link, Switch, Toolbar, Typography } from '@mui/material';
import { makeStyles } from '@mui/styles';
import { Brightness4, BrightnessHigh } from '@mui/icons-material';
import React from 'react';

const useStyles = makeStyles({
  root: {
    flexGrow: 1,
  },
  title: {
    flexGrow: 1,
  },
});

function HomeAppBar(props: HomeAppBarProps) {
  const classes = useStyles();

  return (
    <Fade in={true} timeout={2000}>
      <AppBar color='transparent' className={classes.root} elevation={0} position='static'>
        <Toolbar>
          <Typography color='inherit' className={classes.title} variant='h5'>
            <Link underline='none' href='/'>
              <b>smolurl</b>
            </Link>
          </Typography>
          {!props.isDark ? <Brightness4 /> : <BrightnessHigh />}
          <Switch
            checked={props.isDark}
            onChange={props.toggle}
          />
        </Toolbar>
      </AppBar>
    </Fade>
  );
}

export default HomeAppBar;
