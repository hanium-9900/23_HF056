'use client'

import { ServiceStatisticsResponse } from "@/api";
import dayjs from 'dayjs';
// install (please try to align the version of installed @nivo packages)
// yarn add @nivo/pie
import dynamic from "next/dynamic";

const ResponsiveLine = dynamic(() => import("@nivo/line").then(m => m.ResponsiveLine), { ssr: false });

// make sure parent container have a defined height when using
// responsive component, otherwise height will be 0 and
// no chart will be rendered.
// website examples showcase many properties,
// you'll often use just a few of them.
export default function LineGraph({ statistics, prevStatistics }: { statistics: ServiceStatisticsResponse[], prevStatistics: ServiceStatisticsResponse[] }) {
  const mergedStatistics = [...statistics, ...prevStatistics];

  const data: {
    id: string;
    // color: string;
    data: {
      x: string;
      y: number;
    }[];
  }[] = Object.entries(mergedStatistics.reduce((acc, cur) => {
    const key = `[${cur.method}] ${cur.path}`
    if (!acc[key]) {
      acc[key] = []
    }

    acc[key].push({ ...cur })

    return acc;
  }, {} as {
    [key: string]: typeof mergedStatistics[number][]
  })).map(([key, stat]) => {
    stat.sort((a, b) => a.month - b.month || a.day - b.day)

    const curMonth = new Date().getMonth() + 1;
    const curDate = new Date().getDate();
    // const curDate = 31;
    // let iter = dayjs().startOf('month');
    let iter = dayjs().subtract(7, 'day');
    const data: { x: string, y: number }[] = [];
    while (iter.month() + 1 <= curMonth) {
      console.log(iter)
      data.push({
        x: iter.format('MM-DD'),
        y: stat.find(s => s.month === iter.month() + 1 && s.day === iter.date())?.count || 0
      })
      iter = iter.add(1, 'day');

      if (iter.month() + 1 === curMonth && iter.date() > curDate) break;
    }

    return {
      id: key,
      data
    }
  });

  return (
    <>
      <ResponsiveLine
        data={data}
        margin={{ top: 50, right: 150, bottom: 50, left: 60 }}
        xScale={{ type: 'point' }}
        yScale={{
          type: 'linear',
          min: 0,
          max: 'auto',
          stacked: true,
          reverse: false
        }}
        yFormat=" >-.2f"
        axisTop={null}
        axisRight={null}
        axisBottom={{
          tickSize: 5,
          tickPadding: 5,
          tickRotation: 0,
          legend: '날짜',
          legendOffset: 36,
          legendPosition: 'middle'
        }}
        axisLeft={{
          tickSize: 5,
          tickPadding: 5,
          tickRotation: 0,
          legend: '호출 수',
          legendOffset: -40,
          legendPosition: 'middle'
        }}
        colors={{ scheme: 'set1' }}
        pointSize={10}
        pointColor={{ theme: 'background' }}
        pointBorderWidth={2}
        pointBorderColor={{ from: 'serieColor' }}
        pointLabelYOffset={-12}
        // enableSlices="x"
        useMesh={true}
        legends={[
          {
            anchor: 'bottom-right',
            direction: 'column',
            justify: false,
            translateX: 100,
            translateY: 0,
            itemsSpacing: 0,
            itemDirection: 'left-to-right',
            itemWidth: 80,
            itemHeight: 20,
            itemOpacity: 0.75,
            symbolSize: 12,
            symbolShape: 'circle',
            symbolBorderColor: 'rgba(0, 0, 0, .5)',
            effects: [
              {
                on: 'hover',
                style: {
                  itemBackground: 'rgba(0, 0, 0, .03)',
                  itemOpacity: 1
                }
              }
            ]
          }
        ]}
      />
    </>
  );
}
