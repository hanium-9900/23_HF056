'use client';

import { ServiceStatisticsResponse } from '@/api';
// install (please try to align the version of installed @nivo packages)
// yarn add @nivo/pie
import dynamic from 'next/dynamic';

const ResponsivePie = dynamic(() => import('@nivo/pie').then(m => m.ResponsivePie), { ssr: false });

// make sure parent container have a defined height when using
// responsive component, otherwise height will be 0 and
// no chart will be rendered.
// website examples showcase many properties,
// you'll often use just a few of them.
export default function PieGraph({ statistics /* see data tab */ }: { statistics: ServiceStatisticsResponse[] }) {
  const mergedStatistics = statistics.reduce(
    (acc, cur) => {
      const key = `[${cur.method}] ${cur.path}`;
      if (!acc[key]) {
        acc[key] = {
          api_id: cur.api_id,
          method: cur.method,
          path: cur.path,
          count: cur.count,
        };
      } else {
        acc[key].count += cur.count;
      }
      return acc;
    },
    {} as {
      [key: string]: {
        api_id: number;
        method: string;
        path: string;
        count: number;
      };
    }
  );

  const data: { id: string; label?: string; value: number; color?: string }[] = Object.entries(mergedStatistics).map(([key, s]) => ({
    id: key,
    label: key,
    value: s.count,
    color: `hsl(${s.api_id * 360}, 70%, 50%)`,
  }));

  return (
    <>
      <ResponsivePie
        data={data}
        margin={{ top: 40, right: 40, bottom: 80, left: 40 }}
        sortByValue={true}
        innerRadius={0.6}
        padAngle={2}
        cornerRadius={3}
        activeOuterRadiusOffset={8}
        // colors={['#3b82f6', '#ef4444']}
        colors={{ scheme: 'set1' }}
        borderColor={{
          from: 'color',
          modifiers: [['darker', 0.2]],
        }}
        arcLinkLabelsSkipAngle={10}
        arcLinkLabelsTextColor={{ theme: 'labels.text.fill' }}
        arcLinkLabelsThickness={2}
        arcLinkLabelsColor={{ from: 'color', modifiers: [] }}
        arcLabelsSkipAngle={10}
        arcLabelsTextColor={{
          from: 'color',
          modifiers: [['darker', 2]],
        }}
        legends={[
          {
            anchor: 'bottom',
            direction: 'row',
            justify: false,
            translateX: 0,
            translateY: 56,
            itemsSpacing: 0,
            itemWidth: 100,
            itemHeight: 18,
            itemTextColor: '#999',
            itemDirection: 'left-to-right',
            itemOpacity: 1,
            symbolSize: 18,
            symbolShape: 'circle',
            effects: [
              {
                on: 'hover',
                style: {
                  itemTextColor: '#000',
                },
              },
            ],
          },
        ]}
      />
    </>
  );
}
