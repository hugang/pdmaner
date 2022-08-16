import React, { forwardRef } from 'react';
import { Graph } from '@antv/x6';
import '@antv/x6-react-shape';
import { separator } from '../../../../../profile';
import Tooltip from '../../../tooltip';
import './style/index.less';
import {hex2Rgba} from '../../../../lib/color';

const Table = forwardRef(({node}, ref) => {
  const data = node.data;
  const store = node.store;
  const id = node.id;
  const allFk = node?._model?.getIncomingEdges(id)?.map(t => t.getTargetPortId()
      .split(separator)[0]) || [];
  const onDragOver = (e) => {
    e.preventDefault();
  };
  const onDrop = (e) => {
    store?.data?.updateFields(store.data.originKey, JSON.parse(e.dataTransfer.getData('fields')));
  };
  const validateSelected = (f, {targetPort, sourcePort}) => {
    const fieldTargetPort = `${f.id}${separator}in`;
    const fieldSourcePort = `${f.id}${separator}out`;
    return targetPort === fieldTargetPort
        || targetPort === fieldSourcePort
        || sourcePort === fieldTargetPort
        || sourcePort === fieldSourcePort;
  };
  const calcFKPKShow = (f, h) => {
    if (h.refKey === 'primaryKey') {
      if (f[h.refKey]) {
        return '<PK>';
      } else if (allFk.includes(f.id)) {
        return '<FK>';
      }
    } else if (h.refKey === 'notNull') {
      if (f[h.refKey]) {
        return '<NOTNULL>';
      }
      return '';
    }
    return f[h.refKey];
  };
  const getTitle = () => {
    const tempDisplayMode = data.nameTemplate || '{defKey}[{defName}]';
    return tempDisplayMode.replace(/\{(\w+)\}/g, (match, word) => {
      return data[word] || data.defKey || '';
    });
  };
  const calcColor = () => {
    const color = node.getProp('fillColor') || '#DDE5FF';
    if (color.startsWith('#')) {
      return hex2Rgba(color, 0.05);
    }
    const tempColor = color.replace(/rgb?\(/, '')
        .replace(/\)/, '')
        .replace(/[\s+]/g, '')
        .split(',');
    return `rgba(${tempColor.join(',')}, 0.05)`;
  };
  return <div
    ref={ref}
    className='chiner-er-table'
    onDragOver={onDragOver}
    onDrop={onDrop}
    style={{color: node.getProp('fontColor')}}
  >
    <div
      className='chiner-er-table-header'
      style={{background: node.getProp('fillColor')}}
    >
      {`${getTitle()}${store?.data.count > 0 ? `:${store?.data.count}` : ''}`}
      {
        data?.comment &&
          <Tooltip title={data?.comment} force conversion={1} placement='top'>
            <div className='chiner-er-table-header-icon'>
              <div style={{borderRightColor: node.getProp('fillColor')}}>{}</div>
            </div>
          </Tooltip>
      }
    </div>
    <div
      className='chiner-er-table-body'
      style={{background: calcColor()}}
    >
      {
        data.fields.map((f) => {
          return <div
            key={`${f.id}${f.defName}`}
            className={`${validateSelected(f, store.data) ? 'chiner-er-table-body-selected' : ''} ${f.primaryKey ? 'chiner-er-table-body-primary' : ''}`}>
            {
              data.headers.map((h) => {
                const label = calcFKPKShow(f, h);
                return <span
                  style={{width: data.maxWidth[h.refKey]}}
                  key={h.refKey}
                >
                  {typeof label === 'string' ?
                    label.replace(/\r|\n|\r\n/g, '')
                    : label}
                </span>;
              })
            }
          </div>;
        })
      }
    </div>
  </div>;
});

Graph.registerNode('table', {
  inherit: 'react-shape',
  zIndex: 2,
  attrs: {
    body: {
      stroke: '#DFE3EB',  // 边框颜色
      strokeWidth: 2,
      rx: 5,
      ry: 5,
    },
  },
  component: <Table/>,
});
