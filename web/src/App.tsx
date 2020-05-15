import { createMuiTheme, CssBaseline, Grid, ThemeProvider, useMediaQuery } from '@material-ui/core';
import { blue, pink } from '@material-ui/core/colors';
import React from 'react';
import UrlForm from './form/UrlForm';
import HomeAppBar from './HomeAppBar';
import ShortenedUrl from './ShortenedUrl';

const booleanStrings = new Set<string>(['true', 'false']);

function App() {
  const prefersDarkMode = useMediaQuery('(prefers-color-scheme: dark)');
  const [darkMode, setDarkMode] = React.useState<boolean>(false);
  const [newUrlText, setNewUrlText] = React.useState<string>('');

  const toggleDarkMode = (): void => {
    const newDark = !darkMode;
    setDarkMode(newDark);
    localStorage.setItem('darkMode', JSON.stringify(newDark));
  }

  React.useEffect(() => {
    const darkModeStorage = localStorage.getItem('darkMode');

    if (darkModeStorage !== null && booleanStrings.has(darkModeStorage)) {
      setDarkMode(JSON.parse(darkModeStorage));
    } else {
      setDarkMode(prefersDarkMode);
    }
  }, [prefersDarkMode]);

  const theme = createMuiTheme({
    palette: {
      background: {
        default: darkMode ? '#212121' : '#ffffff'
      },
      primary: {
        main: darkMode ? '#ffffff' : '#212121'
      },
      secondary: {
        main: darkMode ? blue[400] : pink[400]
      },
      type: darkMode ? 'dark' : 'light'
    }
  });

  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <HomeAppBar
        toggle={toggleDarkMode}
        isDark={darkMode}
      />
      <Grid
        container
        direction='column'
        alignItems='center'
        justify='center'
        style={{ minHeight: '75vh' }}>
        <ShortenedUrl
          text={newUrlText}
          visible={newUrlText.length > 0}
        />
        <UrlForm
          setNewUrlText={setNewUrlText}
        />
      </Grid>
    </ThemeProvider>
  );
}

export default App;
