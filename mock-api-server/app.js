import express from 'express';

const app = express();

const bigData = [
  {
    name: 'iPhone 12',
    price: 999000,
    description: 'The latest iPhone model.',
    owner: 'Apple Inc.',
  },
  {
    name: 'Samsung Galaxy S21',
    price: 899000,
    description: 'A flagship smartphone from Samsung.',
    owner: 'Samsung Electronics',
  },
];

app.get('/phone', (req, res) => {
  const key = req.header('X-API-KEY');
  const { corporation } = req.query;

  if (key != '12345') {
    res.status(401).json({ message: 'API Key is Not Valid' });
    return;
  }

  if (corporation == 'apple') {
    res.json(bigData[0]);
    return;
  }

  if (corporation == 'samsung') {
    res.json(bigData[1]);
    return;
  }

  res.status(400).json({ message: 'Wrong Query Parameter' });
});

app.listen(8081, () => {
  console.log('Server is running on port 8081');
});
