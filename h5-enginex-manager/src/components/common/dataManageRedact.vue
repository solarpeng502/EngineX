<style>
	.dataManageRedact {
		width: 100%;
		white-space: nowrap;
	}

	/* .MR_header {

		display: flex;
		justify-content: space-between;
		padding: 20px;
		box-sizing: border-box;
		border-bottom: 1px solid #ddd;
	}

	.MR_header>div:nth-of-type(1) {
		display: flex;
		align-items: center;
		justify-content: space-between;
		width: 12%;
		font-size: 18px;
	}

	.MR_input {
		display: flex;
		flex-wrap: wrap;
		justify-content: flex-start;
		border-bottom: 1px solid #ddd;
		padding: 0 0 20px 0;
	}

	.MR_input>div {
		width: 30%;
		display: flex;
		align-items: center;
		justify-content: space-around;
		margin-left: 2%;
		margin-top: 20px;
	}

	.MR_input>div>p {
		width: 30%;
	} */

	.MR_checkbox {
		padding: 20px;
		border-bottom: 1px solid #ddd;
	}

	.MR_scope {
		padding: 20px;
		border-bottom: 1px solid #ddd;
	}

	.MR_scope>div {
		display: flex;
		align-items: center;
	}

	.MR_scope>div>p {
		width: 10%;
	}

	.MR_derive {

		margin: 0 40px 0 40px;

	}

	.MR_rule_home {
		overflow: scroll;
		overflow-x: hidden;
		height: 350px;
	}

	.MR_rule_home::-webkit-scrollbar {
		display: none;
		/* Chrome Safari */
	}

	.MR_toolbar {
		background-color: #F0F0F0;
		height: 40px;
		display: flex;
		justify-content: space-around;
		align-items: center;
		font-weight: bold;
		font-size: 18px;
		-moz-user-select: none;
		-webkit-user-select: none;
		-ms-user-select: none;
		-khtml-user-select: none;
		user-select: none;

	}

	.MR_toolbar>p:hover {
		color: #fff;

	}
</style>
<template>
	<div class="dataManageRedact" v-loading="loading">
		<div :class="smallHeader?'MR_header MR_headerSmall':'MR_header'">
			<div>
				<div>
					<el-button type="primary" icon="el-icon-arrow-left" circle @click="$emit('close')"></el-button>
				</div>
				<div>
					<span v-if="id===0">新增字段 :</span>
					<span v-else>编辑字段 :</span>
				</div>
			</div>
			<div>

				<el-button :icon="smallHeader?'el-icon-bottom':'el-icon-top'" circle @click="openHeader">
				</el-button>
				<el-button type="success" icon="el-icon-check" circle @click="submit"></el-button>

			</div>
		</div>
		<div :class="smallHeader?'MR_input MR_inputSmall':'MR_input'">
			<div>
				<p>字段名称: </p>
				<el-input placeholder="请输入字段名" maxlength="30" v-model="fieldEn" clearable></el-input>
			</div>
			<div>
				<p>字段中文名: </p>
				<el-input placeholder="请输入字段中文名" maxlength="20" v-model="fieldCn" clearable></el-input>
			</div>
			<div>
				<p>值类型 : </p>
				<el-select v-model="valueType" placeholder="请选择">
					<el-option label="数值型" :value="1" />
					<el-option label="字符型" :value="2" />
					<!-- <el-option label="枚举型" :value="3" /> -->
					<!-- <el-option label="小数型" :value="4" /> -->
					<!-- <el-option label="数组型" :value="5" /> -->
					<el-option label="JSON型" :value="6" />
				</el-select>
			</div>
		</div>
		<div style="flex: 1;overflow: auto;">

			<div v-show="isDerivative=='Derivative'" class="MR_derive">

				<el-tabs v-model="activeName">
					<el-tab-pane label="条件区域" name="first">
						<div class="MR_rule_home">
							<rule :Data="ruledata" @faadd="faadd" @fadelect="fadelect" @sonadd="sonadd"
								@sondelect="sondelect" @change="change"></rule>
						</div>
					</el-tab-pane>
					<el-tab-pane label="公式编辑" name="second">

					</el-tab-pane>
					<el-tab-pane label="groovy脚本" name="third">

					</el-tab-pane>
				</el-tabs>
				<div v-show="activeName!=='first'">
					<teV2 v-model="formula" :hint="activeName=='second'">

					</teV2>
				</div>

			</div>
			<div v-if="Sourcelist">
				<div v-show="isDerivative=='SQL'" class="MR_derive">

					<el-select v-model="SQLType" placeholder="请选择数据源类型" style="margin-top: 20px;width: 200px;"
						@change="SQLName = ''">
						<el-option v-for="item in SourcelistType" :key="item.type" :label="item.type"
							:value="item.type">
						</el-option>
					</el-select>

					<el-select v-model="SQLName" placeholder="请选择数据源" style="margin-top: 20px;margin-left: 20px;">
						<el-option v-for="item in Sourcelist" :key="item.id" :label="item.name" :value="item.id">
						</el-option>
					</el-select>

					<br />
					<p
						style="margin-top: 20px;border-top: 1px dotted #ddd;padding-top: 10px;display: flex;align-items: center;">
						变量：<i class="el-icon-circle-plus-outline" style="color:#409EFF;font-size: 24px;"
							@click="addsqlVariable"></i></p>

					<div v-for="(value,index) in sqlVariable"
						style="display: flex;align-items: center;margin-top: 10px;">
						<el-input v-model="value.key" style="width: 200px;" placeholder="键"></el-input>
						<p style="margin:10px;">:</p>
						<el-input v-model="value.value" style="width: 200px;" placeholder="默认值"></el-input>
						<i class="el-icon-circle-close" :style="{color:'#F56C6C',fontSize: '24px',marginLeft:'10px'}"
							@click="delectsqlVariable(index)"></i>
					</div>


					<p
						style="margin-top: 20px;border-top: 1px dotted #ddd;padding-top: 10px;display: flex;align-items: center;">
						字典变量：<i class="el-icon-circle-plus-outline" style="color:#409EFF;font-size: 24px;"
							@click="addDictVariable"></i></p>

					<div v-for="(value,index) in dictVariable"
						style="display: flex;align-items: center;margin-top: 10px;">
						<el-input v-model="value.key" style="width: 200px;" placeholder="键"></el-input>
						<p style="margin:10px;">:</p>
						<el-select v-model="value.type" placeholder="请选择类型">
							<el-option v-for="item in dictVariableType" :key="item.value" :label="item.label"
								:value="item.value">
							</el-option> 
						</el-select>
						<el-input v-model="value.value" style="width: 200px;margin-left: 20px;" placeholder="格式"></el-input>
						<i class="el-icon-circle-close" :style="{color:'#F56C6C',fontSize: '24px',marginLeft:'10px'}"
							@click="delectDictVariable(index)"></i>
					</div>

					<el-input type="textarea" v-model="SQLItem" rows="9" style="margin-top: 20px;"
						placeholder="请输入SQL语句">
					</el-input>


				</div>
			</div>
			<div v-if="ftype==4">
				<el-select v-model="interfaceId" placeholder="请选择接口" style="margin-top: 20px;margin-left: 20px;">
					<el-option v-for="item in interfaceList" :key="item.id" :label="item.name" :value="item.id">
					</el-option>
				</el-select>
				<el-cascader style="margin-left: 20px;" v-model="interfaceParseField" :key="keynum" :options="dataJson"
					:props="{ checkStrictly: true }" clearable></el-cascader>
			</div>
			<div v-show="valueType==6" class="MR_derive">
				<p style="margin-top: 20px;">JSON:</p>
				<el-input type="textarea" v-model="jsonValue" :rows="9" style="margin-top: 20px;"
					placeholder="请输入格式JSON">
				</el-input>
			</div>
		</div>


	</div>
</template>

<script>
	// import fieldUserTable from './fieldUserTable.vue'
	import '@/assets/css/ManageRedact.css'
	import teV2 from '@/components/common/teV2.vue'
	import mangeRedactMixin from '@/utils/contminxin/MangeRedactMixin.js'
	import {
		getInterfaceList
	} from '@/api/index.js'
	import rule from './rule.vue'
	export default {
		mixins: [mangeRedactMixin],
		components: {
			getInterfaceList,
			// fieldUserTable,
			rule,
			teV2
		},
		props: {
			fieldTypeId: {
				type: Number,
				default: 0,
			},
			setsave: {
				type: Function,
				default: () => {}
			},
			id: {
				type: Number,
				default: 0
			},
			getInfo: {
				type: Function,
				default: () => {}
			},
			updata: {
				type: Function,
				default: () => {}
			},
			ftype: {
				type: Number,
				default: 1
			}
		},

		data() {
			return {
				sqlVariable: [],
				interfaceParseField: [],
				keynum: 1,
				interfaceId: '',
				jsonValue: '',
				loading: false,
				SQLItem: '',
				SQLName: '',
				tempFormula: '',
				formula: '',
				fieldEn: '',
				fieldCn: '',
				valueType: '',
				SQLType: '',
				isDerivative: "",
				isOutput: false,
				activeName: 'first',
				isrecord: false, //开始记录
				text: '', //剪出的字符串
				tempIndex: null, //暂存index
				lest: "", //暂存末尾
				isshow: false,
				islest: true,
				interfaceList: [],
				ruledata: [{
					"fieldSubCond": [{
						"fieldId": '',
						"operator": "",
						"fieldValue": "",
						"logical": ""
					}],
					"conditionValue": "",
					"fieldValue": ""
				}],
				dictVariable: [],
				dictVariableType: [{
					label: '时间',
					value: 'date'
				}]
			}
		},
		created() {
			if (this.ftype == 4) {
				getInterfaceList({
					pageNo: 0,
					pageSize: 0,
					interfaceInfo: {}
				}).then(res => {
					this.interfaceList = res.data.klist

				})
			}
			if (this.ftype == 2) {
				this.isDerivative = 'SQL'
				console.log(1)
			} else if (this.ftype == 3) {
				this.isDerivative = 'Derivative'
			}
			this.$store.dispatch('getfielduser')
			if (this.id != 0) {
				this.loading = true
				this.getInfo(this.id, {}).then(res => {
					this.loading = false
					if (res.status === "1") {
						this.jsonValue = JSON.stringify(JSON.parse(res.data.fieldVo.jsonValue), null, 4)

						this.fieldCn = res.data.fieldVo.fieldCn
						this.fieldEn = res.data.fieldVo.fieldEn
						this.valueType = res.data.fieldVo.valueType
						this.interfaceId = res.data.fieldVo.interfaceId
						this.interfaceParseField = res.data.fieldVo.interfaceParseField ? res.data.fieldVo
							.interfaceParseField.split('.') : ''
						this.fid = res.data.fieldVo.fieldTypeId
						this.isOutput = res.data.fieldVo.isOutput == 1 ? true : false

						if (res.data.fieldVo.isDerivative) {
							this.isDerivative = 'Derivative'
						} else if (res.data.fieldVo.useSql) {
							this.isDerivative = 'SQL'
							this.SQLType = this.$store.state.Sourcelist.find(x => x.id == res.data.fieldVo
								.dataSourceId).type

							this.SQLName = res.data.fieldVo.dataSourceId
							this.SQLItem = res.data.fieldVo.sqlStatement
						}
						if (res.data.hasFormula == "y" || res.data.hasGroovy == "y") {
							this.formula = JSON.parse(res.data.fieldVo.formula)[0].formula
							if (res.data.hasFormula == "y") {
								this.activeName = "second"
							}
							if (res.data.hasGroovy == "y") {
								this.activeName = "third"
							}
						} else if (res.data.fieldVo.fieldCondList.length > 0) {

							this.ruledata = res.data.fieldVo.fieldCondList
						}
						if (this.ftype == 2) {
							if (res.data.fieldVo.sqlVariable == null) {
								this.sqlVariable = []
							} else {
								this.sqlVariable = JSON.parse(res.data.fieldVo.sqlVariable)
							}
							if (res.data.fieldVo.dictVariable == null) {
								this.dictVariable = []
							} else {
								this.dictVariable = JSON.parse(res.data.fieldVo.dictVariable)
							}
							
							
							
							

						}

						// console.log(res.data.fieldVo.fieldCondList)
					}
				}).catch(err => {
					this.loading = false
					this.$message.error('网络出现问题-_-');
				})
			}


		},
		computed: {
			
			dataJson() {
				if (this.ftype != 4) {
					return {}
				}
				let obj = {}
				this.interfaceList.forEach(value => {
					if (value.id == this.interfaceId) {
						obj = JSON.parse(value.responseBody)
					}
				})

				obj = this.deepGetLayout(obj)
				console.log(obj)
				return obj
			},
			FieldUser() {
				return this.$store.state.FieldUser
			},
			SourcelistType() {
				let arr = []
				if (this.$store.state.Sourcelist) {
					this.$store.state.Sourcelist.forEach(value => {
						let sarr = arr.find(x => x.type == value.type)
						if (sarr) {
							sarr.data.push(value)
						} else {
							arr.push({
								type: value.type,
								data: [value]
							})
						}

					})
				}
				console.log(arr)
				return arr
			},
			Sourcelist() {
				let arr = this.SourcelistType.find(x => x.type == this.SQLType)
				if (arr) {
					return arr.data
				} else {
					return []
				}

			}
		},
		mounted() {
			// this.$refs.textarea.$refs.textarea.onkeydown = (e) => {
			// 	if (e.key === 'Backspace') {
			// 		this.text = ""
			// 	}
			// }
		},
		methods: {
			addDictVariable() {
				this.dictVariable.push({
					key: '',
					type: '',
					value: ''
				})
			},
			delectDictVariable(index) {
				this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.dictVariable.splice(index, 1)
					this.$message({
						type: 'success',
						message: '删除成功!'
					});
				})
			},
			delectsqlVariable(index) {
				this.sqlVariable.splice(index, 1)
			},
			addsqlVariable() {
				this.sqlVariable.push({
					value: '',
					key: ''
				})
			},
			deepGetLayout(obj) {
				let sobj = []
				for (let key in obj) {
					if (obj.hasOwnProperty(key)) {
						if (typeof obj[key] == 'object' && !Array.isArray(obj[key]) && obj[key] != null) {
							sobj.push({
								label: key,
								value: key,
								children: this.deepGetLayout(obj[key])
							})

						} else if (typeof obj[key] == 'object' && Array.isArray(obj[key]) && obj[key] != null) {
							sobj.push({
								label: key,
								value: key,
								children: [{
									label: '元素',
									value: '[]',
									children: this.deepGetLayout(obj[key][0])
								}]
							})
						} else {
							sobj.push({
								label: key,
								value: key,
							})
						}
					}
				}
				return sobj
			},
			submit() {
				let reg = /[\u4e00-\u9fa5]+/g;
				if (this.fieldEn.match(reg) != null) {
					this.$message.error('代码不允许出现中文');
					return
				}
				if (this.verificationNameCode(this.fieldEn) || this.verificationNameCode(this.fieldCn)) {
					return
				}
				if (this.fieldEn.trim() == '' || this.fieldCn.trim() == '' || this.valueType == '') {
					this.$message.error('请填入所有字段，并检查空格');
					return
				}
				let is = {
					is:true,
					msg:'请填入页面中未填的部分'
				}
				if (this.isDerivative == "Derivative" && this.activeName == 'first') {
					this.ruledata.forEach(value => {
						if (value.conditionValue.length < 1) {
							is.is = false
						}
						value.fieldSubCond.forEach((item, inde) => {
							if (item.fieldId == "" || item.operator == "" || item.fieldValue.length < 1) {
								is.is = false
							}
							if (item.logical == "" && inde !== value.fieldSubCond.length - 1) {
								is.is = false
							}
						})
					})
				}
				if (this.ftype == 2) {
					this.sqlVariable.forEach(value => {
						if (value.value.trim() === "" || value.key.trim() === "") {
							is.is = false
						}
					})
				
					this.dictVariable.forEach(value => {
						if (value.key.trim() === "" || value.type=== ""|| value.value.trim()=== "") {
							is.is = false
						}
					})
					
					let arr = []
					arr.push(...this.sqlVariable.map(value=>value.key)) 
					arr.push(...this.dictVariable.map(value=>value.key))
					
					if(arr.length!=Array.from(new Set(arr)).length){
						is.is=false
						is.msg = '不允许出现重复的变量'
					}
					
					
				}
				if (this.ftype == 4 && !this.interfaceId) {
					is.is = false
				}
				if (is.is === false) {
					this.$message.error(is.msg);
					return
				}
				if (this.valueType == 6 && !this.isJSON(this.jsonValue)) {
					this.$message.error('请检查JSON格式');
					return
				}

				let obj = {
					searchKey: '',
					fieldEn: this.fieldEn,
					fieldCn: this.fieldCn,
					valueType: this.valueType, //字段值类型
					isDerivative: this.isDerivative == "Derivative" ? 1 : 0, //是否衍生字段
					isOutput: this.isOutput ? 1 : 0, //是否输出字段
					// valueScope: this.valueScope, //字段约束范围
					fieldCondList: '',
					formulaHidden: '',
					isUseSql: this.isDerivative == "SQL" ? true : false, //是否使用sql
					dataSourceId: 0,
					sqlStatement: '', //sql语句
				}
				if (this.valueType == 6) {
					obj.jsonValue = this.jsonValue
				}
				if (this.ftype == 2) {
					if (this.sqlVariable.length == 0) {
						obj.sqlVariable = null
					} else {
						obj.sqlVariable = JSON.stringify(this.sqlVariable)
					}
					obj.dictVariable = JSON.stringify(this.dictVariable)
				}
				if (this.ftype == 4) {
					obj.isInterface = 1
					obj.interfaceId = this.interfaceId
					obj.interfaceParseField = this.interfaceParseField.join('.')
				} else {
					obj.isInterface = 0
				}
				// console.log(this.isDerivative)
				if (this.isDerivative == "Derivative") {
					if (this.activeName == "first") {
						obj.fieldCondList = JSON.stringify(this.ruledata)
					} else if (this.activeName == "second" || this.activeName == "third") {
						let tempArr = []
						let num = 0
						for (let i of this.formula) {
							if (i === "@") {
								num++
							}
						}
						if (num % 2 === 0 && num != 0) {
							this.formula.match(/@.*?@/g).forEach(value => {
								let tempObj = {
									fieldCN: value.substring(1, value.length - 1),
									fieldCond: ''
								}
								tempArr.push(tempObj)
							})
						}
						obj.formulaHidden = JSON.stringify([{
							fvalue: '',
							formula: this.formula.trim(),
							idx: '0',
							farr: tempArr,
						}])
					}

				} else if (this.isDerivative == "SQL") {
					obj.dataSourceId = this.SQLName
					obj.sqlStatement = this.SQLItem

				}
				let isT = true;
				if (this.isDerivative == "SQL") {
					let sqlCheck = obj.sqlStatement.match(/(create|update|delete|truncate|alert|drop)\s+/im);
					if (sqlCheck != null) {
						isT = false;
						this.$message.error('存在有风险sql关键词:' + sqlCheck[0].toUpperCase());
					}
				}
				if (isT) {
					if (this.id == 0) {
						obj.fieldTypeId = this.fieldTypeId == 99999999 ? 0 : this.fieldTypeId,
							this.loading = true
						this.setsave(obj).then(res => {
							this.loading = false
							if (res.status === "1") {
								this.$message({
									message: '添加成功',
									type: 'success'
								});
								this.$emit('Ok')
								// this.$store.dispatch('reGetisOutput')
								this.$store.dispatch('reGetfielduser')
							}
						}).catch(err => {
							this.loading = false
							this.$message.error('网络出现问题-_-');
						})
					} else {
						obj.id = this.id
						obj.fieldTypeId = this.fid
						this.loading = true
						this.updata(obj).then(res => {
							this.loading = false
							if (res.status === "1") {
								this.$message({
									message: '修改成功',
									type: 'success'
								});
								this.$emit('Ok')

								// this.$store.dispatch('reGetisOutput')
								this.$store.dispatch('reGetfielduser')

							}
						}).catch(err => {
							this.loading = false
							this.$message.error('网络出现问题-_-');
						})
					}
				}
			},
			isJSON(str) {
				if (typeof str == 'string') {
					try {
						var obj = JSON.parse(str);
						if (typeof obj == 'object' && obj) {
							return true;
						} else {
							return false;
						}

					} catch (e) {
						console.log('error：' + str + '!!!' + e);
						return false;
					}
				}
			},
			change(index, inde) {
				this.ruledata[index].fieldSubCond[inde].operator = ""
				this.ruledata[index].fieldSubCond[inde].fieldValue = ""
			},
			sondelect(index, inde) {
				this.ruledata[index].fieldSubCond.splice(inde, 1)
			},
			sonadd(index, inde) {
				this.ruledata[index].fieldSubCond.splice(inde + 1, 0, {
					"fieldId": "",
					"operator": "",
					"fieldValue": "",
					"logical": ""
				})
			},
			faadd(index) { //rule父节点添加
				this.ruledata.splice(index + 1, 0, {
					"conditionValue": "",
					"fieldSubCond": [{
						"fieldId": "",
						"operator": "",
						"fieldValue": "",
						"logical": "",



					}]
				})


			},
			fadelect(index) {
				this.ruledata.splice(index, 1)
			},
			textareaAdd(text) {
				if (text === "fx") {
					this.formula = "def main(_){\n\n}"
				} else {
					this.formula += text
				}
			},
			dbclick(e) {
				let T = ""
				T = this.formula.split("")

				T.splice(this.tempIndex, this.text.length + 1, '@' + e + '@')
				this.formula = T.join("")
				this.isshow = false

			}
		},
		watch: {
			interfaceId() {
				this.keynum++
			},
			formula() {
				let num = 0
				for (let i of this.formula) {
					if (i === "@") {
						num++
					}
				}
				if (num % 2 === 1) {
					this.isshow = true
					for (let i in this.formula) {
						if (this.formula[i] !== this.tempFormula[i] && this.formula[i] == "@") {

							if (this.islest) {
								this.tempIndex = i

								this.lest = this.formula.substring(parseInt(this.tempIndex) + 1, this.formula.length)
								this.islest = false
							}
							// console.log('字段：' + this.formula[i] + "暂存字段：" + this.tempFormula[i])
							break
						}
					}

					let T = this.formula.substring(parseInt(this.tempIndex) + 1, this.formula.length)
					if (this.lest !== "") {
						// console.log(T)
						T = T.substring(0, T.indexOf(this.lest))

					} else {
						T = T.substring(0, T.length)
					}

					this.text = T

					// console.log('lest:' + this.lest, 'index:' + this.tempIndex, "T:" + T)

				} else {
					this.islest = true
					this.text = ""
					this.tempIndex = null
					this.isshow = false
				}





				this.tempFormula = this.formula
			}
		}











	}
</script>
