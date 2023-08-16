const { override, fixBabelImports, addWebpackAlias } = require('customize-cra')
const path = require('path')
// npm i react-app-rewired customize-cra -D
module.exports = override(
    fixBabelImports('import', {
        libraryName: 'antd',
        libraryDirectory: 'es',
        style: 'css'
    }),
    addWebpackAlias({
        ['@']: path.resolve(__dirname, 'src')
    })
)