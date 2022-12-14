/*
  获取国际化
*/
import React, { useContext } from 'react';
import _ from 'lodash/object';

import allLangData from '../../lang';
import { CONFIG } from '../../lib/variable';
import { ConfigContent } from '../../lib/context';
import { getMemoryCache } from '../../lib/cache';

const getMessage = ({ lang, id, defaultMessage, format, data }) => {
  const reg = /\{(\w+)\}/g; // 国际化变量替换 格式为 {变量名} data中的变量名与之匹配
  // 初始化时还未获取设定内容，初始设置
  const config = getMemoryCache(CONFIG) || { lang: 'jp' };
  // 未传语言参数时，默认使用variable中自定义内容
  const langData = allLangData[lang || config.lang];
  const message = _.get(langData, id, defaultMessage);
  const defaultFormat = () => {
    if (data) {
      return message.replace(reg, (...replaces) => {
        return data[replaces[1]];
      });
    }
    return message;
  };
  return format ? format(message) : defaultFormat();
};

const string = (params) => {
  // 支持非react组件 纯字符串
  return getMessage(params);
};

const FormatMessage = React.memo(({ id = '', format, defaultMessage = '', data }) => {
  const { lang } = useContext(ConfigContent);
  return getMessage({ lang, id, defaultMessage, format, data });
});

FormatMessage.string = string;

export default FormatMessage;
