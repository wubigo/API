module.paths.push('D:/downloads/node-v12.11.0-win-x64/node_modules');

const weiboer = require('weiboer');
const path = require('path');
const configFile = path.resolve(__dirname, './config.json'); // 微博账号配置,
// configFile 可以缺省，但要至少确保环境变量中设置了username, password
const weiboHelper = weiboer.init(configFile);
weiboHelper.publish('微博内容');