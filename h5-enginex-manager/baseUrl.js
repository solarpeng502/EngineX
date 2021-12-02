// console.log(process.env)
var proxyObj = {}
// console.log(process.env.VUE_PROXY)
switch (process.env.NODE_ENV) {
	case 'development': // 开发环境代理地址
		proxyObj = {
			'/Riskmanage': {
				target: '', // 开发环境
				changeOrigin: true, // 是否跨域
				pathRewrite: {
					'^/Riskmanage': '/Riskmanage'
				},
			
			}
		}
		break
	
}

module.exports = proxyObj
