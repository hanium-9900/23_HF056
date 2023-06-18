import express from 'express';

const app = express();

const bigData = [
  {
    id: 1,
    name: 'iPhone 12',
    price: 999,
    category: 'Electronics',
    description: 'The latest iPhone model.',
    timestamp: '2023-06-18 10:00:00',
    location: {
      latitude: 37.12345,
      longitude: 127.6789,
    },
    tags: ['Apple', 'Smartphone'],
    metadata: {
      source: 'Apple Store',
      owner: 'Apple Inc.',
    },
  },
  {
    id: 2,
    name: 'Samsung Galaxy S21',
    price: 899,
    category: 'Electronics',
    description: 'A flagship smartphone from Samsung.',
    timestamp: '2023-06-18 11:00:00',
    location: {
      latitude: 37.54321,
      longitude: 127.09876,
    },
    tags: ['Samsung', 'Smartphone'],
    metadata: {
      source: 'Samsung Store',
      owner: 'Samsung Electronics',
    },
  },
];

app.get('/bigdata', (req, res) => {
  const key = req.query.key;

  if (key != '12345') {
    res.status(400).json({ message: 'API Key is Not Valid' });
    return;
  }

  res.json(bigData);
});

app.listen(8081, () => {
  console.log('Server is running on port 8081');
});
