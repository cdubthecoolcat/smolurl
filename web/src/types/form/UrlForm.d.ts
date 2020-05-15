interface UrlFormProps {
  setNewUrlText: Function;
};

interface UrlState {
  text: string;
  hasError: boolean;
  errorMessage: string;
};

interface AliasState extends UrlState {
  visible: boolean;
};

