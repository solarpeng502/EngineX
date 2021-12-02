import Vue from 'vue';
import Router from 'vue-router';

Vue.use(Router);

export default new Router({
	routes: [{
			path: '/redirect/:path(.*)',
			component: () => import('../components/page/redirect/index.vue'),
		},
		{
			path: '/',
			redirect: '/dashboard'
		},
		{
			path: '/login',
			component: () => import('../components/page/Login.vue'),
			meta: {
				title: '登录'
			}
		},
		{
			path: '/',
			component: () => import('../components/common/Home.vue'),
			meta: {
				title: '自述文件'
			},
			children: [{
					path: '/dashboard',
					component: () => import('../components/page/Dashboard.vue'),
					meta: {
						title: '系统首页'
					}
				},
				{
					path: '/Knowledge',
					component: () => import('../components/page/Knowledge.vue'),
					meta: {
						title: '复杂规则集'
					}
				},
				
				{
					path: '/dataManage',
					component: () => import('../components/page/Datamanage.vue'),
					meta: {
						title: '基础指标'
					}
				}, {
					path: '/SQLmanage',
					component: () => import('../components/page/SQLManage.vue'),
					meta: {
						title: '数据源指标'
					}
				},
				
			
				{
					path: '/userManagement',
					component: () => import('../components/page/systemManagement/userManagement.vue'),
					meta: {
						title: '用户管理'
					}
				},
				{
					path: '/roleManagement',
					component: () => import('../components/page/systemManagement/roleManagement.vue'),
					meta: {
						title: '角色管理'
					}
				},
				{
					path: '/resourceManagement',
					component: () => import(
						'../components/page/systemManagement/resourceManagement.vue'),
					meta: {
						title: '资源管理'
					}
				},
				{
					path: '/organizationManagement',
					component: () => import(
						'../components/page/systemManagement/organizationManagement.vue'),
					meta: {
						title: '组织管理'
					}
				},
				{
					path: '/logManagement',
					component: () => import('../components/page/systemManagement/logManagement.vue'),
					meta: {
						title: '日志管理'
					}
				},
			
			
				{
					path: '/portManage',
					component: () => import('@/components/page/portManage.vue'),
					meta: {
						title: '接口指标'
					}
				},
				{
					path: '/DataSource',
					component: () => import('@/components/page/DataSource.vue'),
					meta: {
						title: '数据源管理'
					}
				},
				{
					path: '/portSource',
					component: () => import('@/components/page/portSource.vue'),
					meta: {
						title: '接口管理'
					}
				},
				
				{
					path: '/404',
					component: () => import('../components/page/404.vue'),
					meta: {
						title: '404'
					}
				},
				{
					path: '/403',
					component: () => import('../components/page/403.vue'),
					meta: {
						title: '403'
					}
				}
			]
		},

		{
			path: '*',
			redirect: '/404'
		}
	]
});
