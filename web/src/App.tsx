import { createTheme, CssBaseline, ThemeProvider, useMediaQuery } from '@mui/material';
import { blue, pink } from '@mui/material/colors';
import Grid from '@mui/material/Unstable_Grid2'
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

  const theme = createTheme({
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
      mode: darkMode ? 'dark' : 'light'
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
        justifyContent='center'
        alignItems='center'
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
