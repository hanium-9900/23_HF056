import express from 'express';

const app = express();

const bigData = [
  {
    id: 1,
    name: '데이터1',
    value: 10,
    category: '카테고리1',
    description: '이것은 데이터1입니다.',
    timestamp: '2023-06-18 10:00:00',
    location: {
      latitude: 37.12345,
      longitude: 127.6789,
    },
    tags: ['태그1', '태그2'],
    metadata: {
      source: '데이터 출처',
      owner: '데이터 소유자',
    },
  },
  {
    id: 2,
    name: '데이터2',
    value: 20,
    category: '카테고리2',
    description: '이것은 데이터2입니다.',
    timestamp: '2023-06-18 11:00:00',
    location: {
      latitude: 37.54321,
      longitude: 127.09876,
    },
    tags: ['태그3', '태그4'],
    metadata: {
      source: '데이터 출처',
      owner: '데이터 소유자',
    },
  },
];

app.get('/bigdata', (req, res) => {
  res.json(bigData);
});

app.listen(8081, () => {
  console.log('Server is running on port 8081');
});
