import express from 'express';
import morgan from 'morgan';

const app = express();

app.use(morgan('tiny'));

const weatherData = {
  date: '2023-06-23',
  temperature: 25,
  humidity: 60,
  condition: '맑음',
};

app.get('/weather', (req, res) => {
  const key = req.header('X-API-KEY');
  let { daysAgo } = req.query;

  daysAgo = parseInt(daysAgo);

  const currentDate = new Date();
  currentDate.setDate(currentDate.getDate() - daysAgo);
  const targetDate = currentDate.toISOString().split('T')[0];

  weatherData.date = targetDate;

  if (key != '12345') {
    res.status(401).json({ message: 'API Key is Not Valid' });
    return;
  }

  if (!Number.isInteger(daysAgo)) {
    res.status(427).json({ message: '<daysAgo> must be integer' });
    return;
  }

  if (daysAgo < 0) {
    res.status(430).json({ message: '<daysAgo> must be positive' });
    return;
  }

  if (daysAgo >= 0) {
    res.json(weatherData);
    return;
  }

  res.status(400).json({ message: 'Wrong Query Parameter' });
});

app.listen(8081, () => {
  console.log('Server is running on port 8081');
});
