export const submitUrl = async (url: UrlPostData) => {
  const response = await fetch('/api/urls', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(url)
  });
  return await response.json();
};
