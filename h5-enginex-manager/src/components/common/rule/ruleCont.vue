
<template>
	<div class="cont_cont">

		<div class="cont_left" v-loading="leftloading" v-if="!listRedact">
			<div class="cont_header">
				<p class="cont_header_title">{{title}}</p>
				<p class="cont_header_subtitle">{{title}}</p>
			</div>
			<div class="cont_new_file">
				<div v-if="!newf" @click="newFile"><i class="el-icon-folder-add" @click="newFile" style="margin-right: 10px;"></i>新建文件夹</div>
				<div v-else style="padding: 5px;box-sizing:border-box;">
					<div style="display: flex;align-items: center;">
						<i class="el-icon-folder-add" @click="newFile" style="margin-right: 10px;"></i>
						<el-input v-model="tempNewF" placeholder="请输入新文件夹名字,最长20个字符" size="small"></el-input>
					</div>
					<el-button style="margin-left: 60px;margin-top: 10px;" type="danger" icon="el-icon-close" circle size="mini"
					 @click="newf=false;tempNewF=''"></el-button>
					<el-button type="success" icon="el-icon-check" circle size="mini" @click="newFileSure"></el-button>
				</div>
			</div>
			<div class="cont_list">
			
				<fileHome :data="list" @curr="clickCurrid" :currid="currid" @RenameFun="RenameFun" @RenameClose="RenameClose"
				 @updatafilelist="updatafilelist" @delectFun="delectFun">
				</fileHome>


			</div>
		</div>

		<div class="cont_right" v-loading="contloading" @click="tempHintLeft=null;tempHintTop=null;">
			<div v-if="!listRedact">
				<div v-if="showRight===false" class="cont_right_hint">
					请先选择左侧文件夹
				</div>
				<div v-else>
					<div class="cont_right_top">
						<div>
							<el-button type="primary" @click="listRedact=true" :disabled="currid!=99999999?false:'disabled'">新增</el-button>
							<el-button type="danger" @click="using(-1)" :disabled="this.selection.length>0?false:'disabled'">删除</el-button>
							<el-button type="success" @click="using(1)" :disabled="this.selection.length>0?false:'disabled'">启用</el-button>
							<el-button type="warning" @click="using(0)" :disabled="this.selection.length>0?false:'disabled'">停用</el-button>
							<el-select v-model="tempMove" placeholder="移动到:" style="margin-left: 10px;" :disabled="this.selection.length>0?false:'disabled'"
							 filterable @change="mixinMoveChange">
								<el-option v-for="value in listunfold" :key="value.id" :label="value.name" :value="value.id" v-show="value.id!=99999999"></el-option>
							</el-select>
						</div>
						<div style="display: flex;">
							<el-input placeholder="请输入搜索内容" v-model="search">
								<i slot="suffix" class="el-input__icon el-icon-search" @click="getsearch"></i>
							</el-input>
							<el-button style="margin-left: 10px;" @click="upShow=true" v-if="getData.type==1">批量导入</el-button>
						</div>
					</div>
					<div class="cont_right_cont">
						<div v-if="data">
							<el-table border :data="data.data.klist" @select-all="selectAll" @select="select" style="width: 100%"
							 :cell-style="{padding: '10px'}">
								<el-table-column type="selection" width="70">
								</el-table-column>
								<el-table-column v-for="item in getData.row" :key="item.id" :prop="item.row" :label="item.label" align="center">
									<template slot-scope="scope">
										<span v-if="item.type==='Blooen'">
											{{scope.row[item.row]?"是":"否"}}
										</span>
										<span v-else-if="item.type==='State'">
											{{scope.row[item.row]==1?'启用':'未启用'}}
										</span>
										<span v-else-if="item.type==='Time'" style="white-space: nowrap;" class="contText">{{
													new Date(scope.row[item.row]).toLocaleDateString().replace(/\//g, "-") + " " + new Date(scope.row[item.row]).toTimeString().substr(0, 8)
													}}</span>
										<span class="contText" v-else>
											{{scope.row[item.row]}}
										</span>
									</template>
								</el-table-column>

								<el-table-column label="操作" align="center" size="s">
									<template slot-scope="scope">
										<el-button icon="el-icon-setting" circle size="mini" @click="dialogShow(scope.row.id)"></el-button>
									</template>
								</el-table-column>
							</el-table>
							<el-pagination style="float: right;margin-right: 40px;margin-top: 40px;" :current-page="currPage"
							 @current-change="clickpage" background layout="prev, pager, next" :total="data.data.pageInfo.total">
							</el-pagination>
						</div>







					</div>
				</div>
			</div>
			<div v-else style="height: 100%;overflow: hidden;">
				<easyDataManageRedact @close="listRedact=false;tempRedactId=0" @Ok="listRedact=false;tempRedactId=0;getlist()"
				 :updata="getData.updatafield" :id='tempRedactId' :nameId="currid" :setsave="getData.setsave" :getInfo="getData.getInfo"
				 :type="getData.type" v-if="getData.type==1">
				</easyDataManageRedact>

				<dataManageRedact @close="listRedact=false;tempRedactId=0" @Ok="listRedact=false;tempRedactId=0;getlist()" :getData="getData"
				 :id='tempRedactId' :nameId="currid" :type="getData.type" v-if="getData.type==2">
				</dataManageRedact>
				
			
			</div>
		</div>
		<el-dialog title="上传文件" :visible.sync="upShow" width="30%" :before-close="upShowClose">
			<div style="margin: 0 auto;display: flex;justify-content: center;">
				<el-upload class="upload-demo" ref="upload" action="doUpload" :limit="1" :file-list="fileList" :before-upload="beforeUpload"
				 v-loading="Uploadloading">
					<el-button slot="trigger" size="small" type="primary">选取文件</el-button>
					<div slot="tip" class="el-upload__tip">只能上传excel文件，且不超过5MB</div>
					<div slot="tip" class="el-upload-list__item-name">{{fileName}}</div>
				</el-upload>
			</div>
			<span slot="footer" class="dialog-footer">
				<el-button @click="upShow = false">取消</el-button>
				<el-button type="primary" @click="submitUpload();" :disabled="fileName?false:'disabled'">确定</el-button>
			</span>


		</el-dialog>
		<el-dialog title="导入结果" :visible.sync="upCallbackShow" width="60%" close-on-click-modal close-on-press-escape
		 show-close>
			<p style="font-size: 18px;font-weight: bold;">
				导入成功{{callbackresult.sucRows}}条,失败{{callbackresult.failRows}}条,重复{{callbackresult.repeatRows}}条,已存在{{callbackresult.existRows}}条
			</p>
			<div style="">
				<div style="width:98%;border-bottom: 1px dashed #ddd;padding: 1%;">
					<p style="font-weight: bold;font-size: 16px;">文件夹不存在({{callbackresult.result.folderNotExistList.length}})</p>
					<p v-for="value in callbackresult.result.folderNotExistList" style="margin-top: 10px;">{{value}}</p>
				</div>
				<div style="width:98%;border-bottom: 1px dashed #ddd;padding: 1%;">
					<p style="font-weight: bold;font-size: 16px;">系统中已存在的代码({{callbackresult.result.existCodeList.length}})</p>
					<p v-for="value in callbackresult.result.existCodeList" style="margin-top: 10px;">
						{{value}}
					</p>
				</div>
				<div style="width:98%;border-bottom: 1px dashed #ddd;padding: 1%;">
					<p style="font-weight: bold;font-size: 16px;">系统中已存在的名称({{callbackresult.result.existNameList.length}})</p>
					<p v-for="value in callbackresult.result.existNameList" style="margin-top: 10px;">{{value}}</p>
				</div>

				<div style="width:98%;border-bottom: 1px dashed #ddd;padding: 1%;">
					<p style="font-weight: bold;font-size: 16px;">表格中重复的代码({{callbackresult.result.rpCodeList.length}})</p>
					<p v-for="value in callbackresult.result.rpCodeList" style="margin-top: 10px;">{{value}}</p>
				</div>
				<div style="width:98%;border-bottom: 1px dashed #ddd;padding: 1%;">
					<p style="font-weight: bold;font-size: 16px;">表格中重复的名称({{callbackresult.result.rpNameList.length}})</p>
					<p v-for="value in callbackresult.result.rpNameList" style="margin-top: 10px;">{{value}}</p>
				</div>
			</div>
			<span slot="footer" class="dialog-footer">
				<el-button @click="upCallbackShow = false">取消</el-button>
			</span>


		</el-dialog>



	</div>



</template>

<script>
	import '@/assets/css/cont.css'
	import dataManageRedact from '@/components/common/rule/ruleManageRedact.vue'
	import easyDataManageRedact from '@/components/common/rule/easyRuleManageRedact.vue'

	import fileHome from '@/components/common/fileHome.vue'
	import contmixin from '@/utils/contminxin/contmixin.js'
	export default {
		mixins: [
			contmixin
		],
		components: {
			easyDataManageRedact,
	
			// file,
			dataManageRedact,
			fileHome
		},
		created() {
			this.getData.getTree({
				parentId: 0,
				treeType:this.getData.treeType,
				type: 1
			}).then(res => {
				this.list = this.listTreeDeep(res.data, 1)
				this.clickCurrid(99999999)
			})

			this.$store.dispatch('getfielduser')
		},
		props: {
			title: {
				type: String,
				default: ''
			},
			getData: {
				type: Object,
				default: null
			}
		},
		watch: {
			list() {
				if (this.list.length > 0) {
					this.leftloading = false
				}
			}
		},
		data() {
			return {
				list: [],
				callbackresult: {
					sucRows: '',
					failRows: '',
					repeatRows: '',
					existRows: '',
					result: {
						existCodeList: [],
						existNameList: [],
						folderNotExistList: [],
						rpCodeList: [],
						rpNameList: []
					}
				},
				upCallbackShow: false,
				Uploadloading: false,
				tempMove: '',
				leftloading: false, //暂时关闭loading
				search: "",
				fileName: "",
				fileList: [],
				upShow: false,
				currPage: 1,
				currid: null,
				data: null,
				contloading: false,
				newf: false,
				tempNewF: "",
				listRedact: false, //新增页面开启
				tempRedactId: 0,
				selection: []
			}
		},
		methods: {
			getsearch() {
				this.contloading = true
				let params = {

					key: "ruleName",
					status: "0,1",
					"parentIds": String(this.currid) === "99999999" ? '' : String(this.currid),
					"pageNum": 1,
					isSearch: 1,
					ruleInfo: {

						name: this.search,
					}
				}
				this.getData.getlist(params).then(res => {
					this.data = res
					this.contloading = false
				})
				this.currPage = 1
			},
			getlist() {
				this.contloading = true
				this.listRedact = false
				let params = {
					status: "0,1",
					ruleInfo: {},
					"pageNum": 1
				}
				if (String(this.currid) !== "99999999") {
					params.ruleInfo.parentId = this.currid
				}
				this.getData.getlist(params).then(res => {
					this.data = res
					this.contloading = false
				})

			},
			clickpage(e) {
				this.currPage = e
				this.contloading = true
				let params = {
					status: "0,1",
					ruleInfo: {},
					"pageNum": e
				}
				if (String(this.currid) !== "99999999") {
					params.ruleInfo.parentId = this.currid
				}
				this.getData.getlist(params).then(res => {
					if (res.status == "1") {
						this.data = res
						this.contloading = false
						this.selection = []
					}
				})
			},
			newFileSure() {
				this.leftloading = true
				let params = {
					parentId: String(this.currid),
					name: this.tempNewF,
					"treeType": this.getData.treeType,
					"type": "1",
					"engineId": ""
				}
				// if (this.getData.type == 2) {
				// 	params.treeType = '5'
				// }
				this.mixnewFileSure(params)
			},
			
		}



	}
</script>
