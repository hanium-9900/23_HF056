import express from 'express';

const app = express();

const bigData = [
  {
    name: 'iPhone 12',
    price: 999000,
    category: 'Electronics',
    description: 'The latest iPhone model.',
    owner: 'Apple Inc.',
  },
  {
    name: 'Samsung Galaxy S21',
    price: 899000,
    category: 'Electronics',
    description: 'A flagship smartphone from Samsung.',
    owner: 'Samsung Electronics',
  },
];

app.get('/phone', (req, res) => {
  const key = req.header('X-API-KEY');

  if (key != '12345') {
    res.status(400).json({ message: 'API Key is Not Valid' });
    return;
  }

  res.json(bigData);
});

app.get('/phone/apple', (req, res) => {
  const key = req.header('X-API-KEY');

  if (key != '12345') {
    res.status(400).json({ message: 'API Key is Not Valid' });
    return;
  }

  res.json(bigData[0]);
});

app.get('/phone/samsung', (req, res) => {
  const key = req.header('X-API-KEY');

  if (key != '12345') {
    res.status(400).json({ message: 'API Key is Not Valid' });
    return;
  }

  res.json(bigData[1]);
});

app.listen(8081, () => {
  console.log('Server is running on port 8081');
});
