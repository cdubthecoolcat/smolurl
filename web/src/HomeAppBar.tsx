import { AppBar, Fade, Link, Switch, Toolbar, Typography } from '@material-ui/core';
import { makeStyles } from '@material-ui/core/styles';
import { Brightness4, BrightnessHigh } from '@material-ui/icons';
import React from 'react';

interface HomeAppBarProps {
  isDark: boolean;
  toggle: () => void;
};

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
