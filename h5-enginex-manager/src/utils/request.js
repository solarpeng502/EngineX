import axios from 'axios'
import router from '../router';
import ElementUI from 'element-ui';
var url = window.location.origin || window.location.protocol + '//' + window.location.hostname + (window.location.port ?
	':' + window.location.port : '')

const instance = axios.create({
	baseURL: process.env.NODE_ENV === 'produce' || process.env.NODE_ENV === 'release' || process.env.NODE_ENV === 'test' ||
		process.env.NODE_ENV === 'development' || process.env.NODE_ENV === 'jia'|| process.env.NODE_ENV === 'niu' ? '/' : url,
	timeout: 500000,
})
instance.interceptors.request.use((config) => {
	// config.headers['AAA'] = 'AAA';

	// console.log(config)
	deepTirm(config)

	if (config.data) {
		if (config.data.getexcel) {
			config.headers['responseType'] = 'blob'
		}
	}
	if (localStorage.getItem('token')) {
		config.headers['token'] = localStorage.getItem('token');
	}
	return config
})
instance.interceptors.response.use((response) => {
	const {
		data,
		config
	} = response
	// console.log(response)

	let result = data

	if (response.data.status === "0") {
		if (response.data.error === "01000103") {
			if (document.getElementsByClassName('el-message').length === 0) {
				ElementUI.Message.error(response.data.msg);
				router.push({
					path: '/login',
				})
			}
			
		} else {
			ElementUI.Message.error(response.data.msg);
		}
	}


	return result
}, (error) => {
	// console.log('error', error)
	if (error.message.match(/timeout/)) {
		ElementUI.Message.error('请求超时,请稍后再试！');
	} else if (error.response.status === 500) {
		ElementUI.Message.error('连接失败,请稍后再试！');
	} else if (error.response.status === 502) {
		ElementUI.Message.error('网关超时,请稍后再试！');
	} else {
		ElementUI.Message.error('连接失败,请稍后再试！');
	}
	return Promise.reject(error)
});

function deepTirm(e) {
	Object.keys(e).forEach(value => {
		if (typeof e[value] == 'string' && (e[value][0] === " " || e[value][e[value].length - 1] === " ")) {
			e[value] = e[value].trim()
		}
		if (isJSON(e[value])) {
			e[value] = JSON.stringify(deepTirm(JSON.parse(e[value])))
		}
		if (typeof e[value] === "object" && e[value] !== null) {
			if (Array.isArray(e[value])) {
				e[value].forEach(item => {
					if (typeof item === 'string' && (item[0] === " " || item[item.length - 1] === " ")) {
						item = item.trim()
					}
					if (typeof item === 'object') {
						item = deepTirm(item)
					}
				})
			} else {
				deepTirm(e[value])
			}
		}
	})
	return e
}

function isJSON(str) {
	if (typeof str == 'string') {
		try {
			var obj = JSON.parse(str);
			if (typeof obj == 'object' && obj) {
				return true;
			} else {
				return false;
			}

		} catch (e) {
			return false;
		}
	}
}


export default instance
