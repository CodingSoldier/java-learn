装mongoDB
	docker pull mongo:4.2
	mkdir -p /mymongo/data
	docker run --restart always --name mymongo -p 27017:27017 -v /mymongo/data:/data/db -d mongo:4.2

安装MongoDB管理界面 Mongo Express
	docker pull mongo-express
	docker run  --restart always --link mymongo:mongo -p 8081:8081 -d mongo-express

	访问  http://192.168.3.178:8081/
	默认有admin、config、local 3个数据库，不建议使用默认的数据库

进入mongo shell   
	docker exec -it mymongo mongo
	print('hello')   使用js打印hello
	exit    退出

设置账号密码
db.createUser({ user: 'root', pwd: 'poly2017', roles: [ { role: "userAdminAnyDatabase", db: "admin" } ] });	

use test    使用test数据库
show collections      展示集合	

创建一个文档
db.<collection>.insertOne(
	<document>,
	{
		writeConcern: <document>
	}
)
	collection 集合名称
	document 文档内容
	writeConcern 安全写级别，不写则使用默认级别

创建一个文档，例子
db.accounts.insertOne(
	{
		name: "alice11",
		balance: 1001
	}
)

捕获异常
try{
	db.accounts.insertOne(
		{
			_id: "account1",
			name: "aaa111",
			balance: 100
		}
	)
}catch(e){
	print(e)
}

自动生成主键
db.accounts.insertOne(
	{
		name: "bbb",
		balance: 20
	}
)
自动生成主键  "insertedId" : ObjectId("5ee1a3ec1b73733931408920")

一次创建多个文档
db.<collection>.insertMany(
	[<document1>, <document2>],
	{
		writeConcern: <document>,
		ordered: <boolean>
	}
)
ordered排序级别
	ordered=false，打乱文件的写入顺序，优化写入性能
	ordered=true，默认，顺序写入

一次创建多个文档,例子
db.accounts.insertMany(
	[{
		name: "bbb11",
		balance: 210
	},{
		name: "bbb22",
		balance: 220
	}]
)

批量写入，中间主键重复，第一个插入成功，后面插入失败
try{
	db.accounts.insertMany(
		[{
			name: "bbb2",
			balance: 210
		},{
			_id: "account1",
			name: "bbb22",
			balance: 220
		},{
			name: "b33",
			balance: 210
		}]
	)
}catch(e){
	print(e)
}

批量写入，乱序，中间主键重复，成功插入2个
try{
	db.accounts.insertMany(
		[{
			name: "bbb2",
			balance: 210
		},{
			_id: "account1",
			name: "bbb22",
			balance: 220
		},{
			name: "b33",
			balance: 210
		}],{
			ordered: false
		}
	)
}catch(e){
	print(e)
}

插入单个、多个文档，insert不会返回文档id，但是支持explain()命令
db.<colleciton>.insert(
	<document or array of documents>,
	{
		writeConcern: <document>,
		ordered: <boolean>
	}
)

db.accounts.insert(
	{
		name: "b33",
		balance: 210
	}
)

插入多个文档
db.accounts.insert(
	[{
		name: "b33",
		balance: 210
	},{
		name: "b33",
		balance: 210
	}]
)

save命令，内部也是用insert()命令
db.<collection>.save(
	<document>,
	{
		writeConcern: <document>
	}
)

获取主键创建时间，时间是UTC时间 
ObjectId("5ee1a52e1b73733931408921").getTimestamp()

复合主键，使用文档作为文档主键。复合主键任然要满足文档主键唯一性
db.accounts.insert(
	{
		_id: {accountNo: "001", type: "savings"},
		name: "skdfkkkk",
		balance: 90
	}
)

换了{accountNo: "001", type: "savings"}元素位置也能插入成功
db.accounts.insert(
	{
		_id: {type: "savings", accountNo: "001"},
		name: "skdfkkkk",
		balance: 90
	}
)


读取文档
db.<collection>.find(<query>, <projection>)
<projection>文档定义了堆读取结果进行投影（投影：读取部分字段）

读取全部文档，不筛选，不投影
db.accounts.find()

好看点
db.accounts.find().pretty()

匹配查询
db.accounts.find({name: "alice"})
db.accounts.find({name: "alice", balance: 100})

查询复合主键文档，已有复合文档如下
	{
		_id: {type: "savings", accountNo: "001"},
		name: "skdfkkkk",
		balance: 90
	}
	查询
	db.accounts.find({"_id.type":"savings"})

比较操作符：
	{<field>: {$<operator>:<value>}}
	$eq、$ne

匹配查询比比较符查询更加简洁
名字为alice的文档
db.accounts.find({name: {$eq: "alice"}})

db.accounts.find({name: {$ne: "alice"}})

文档不包含 _id.type 字段，使用$ne也会被查询出来
db.accounts.find({"_id.type": {$ne: "savings"}})

大于
db.accounts.find({balance: {$gt: 80}})

$in 匹配字段值与任一查询值相等的文档。名字是alice或者b33
	db.accounts.find({name: {$in: ["alice", "b33"]}})

$nin 匹配字段值与任何查询值都不相等的文档。名字既不是alice也不是b33
同样也会查询出不包含name字段的文档
	db.accounts.find({"name": {$nin: ["alice", "b33"]}})

{逻辑操作符 : {表达式}}。
	db.accounts.find({"balance": {$not: {$lt: 200}}})
		{$not: {$lt: 200}} 不小于200。同样的非操作符也会筛选出不包含字段的文档
	
	$and操作符
	db.accounts.find({
		$and: [
			{"balance": {$gt: 100}},
			{"name": "b33"}
		]
	})		
	
	$and的简写方式
	db.accounts.find({
		"balance": {$gt: 100},
		"name": "b33"
	})
	
	一个字段大于小于某个值
	db.accounts.find({"balance": {$gt: 100, $lt: 500}})

文档包含某字段
	db.accounts.find({"_id.type": {$exists: true}})
_id.type存在，且值不等于savings
	db.accounts.find({"_id.type": {$exists: true, $ne: "savings"}})

主键值是字符串的文档
	db.accounts.find({"_id": {$type: "string"}})
	db.accounts.find({"_id": {$type: ["string",'object']}})

	name值是null类型的文档
	db.accounts.find({"name": {$type: "null"}})

插入包含数组的文档
	db.accounts.insert([
		{
			name: "jack",
			balance: 20010,
			contact: ["11211", "Alabam2a", "US"]
		},
		{
			name: "karen",
			balance: 250220,
			contact: [["1111", "22222"], "Beijing", "China"]
		}
	])

使用$all操作数组，contact字段包含"China", "Beijing"
	db.accounts.find({"contact": {$all: ["China", "Beijing"]}})

数组里面嵌套数组，contact包含数组["1111", "22222"]
	db.accounts.find({"contact": {$all: [["1111", "22222"]]}})

$elemMatch操作符，$gt、$lt的值必须是字符串类型
	db.accounts.find({"contact": {$elemMatch: {$gt: "1000", $lt: "20000"}}})

正则表达式操作符
	name以c或j开头
	db.accounts.find({name: {$in: [/^c/, /^j/]}})

	第二种正则表达式的写法，忽略大小写
	db.accounts.find({name: {$regex: /ALI/, $options: 'i'}})

db.<collection>.find()返回一个文档集合游标，在不迭代游标的情况下，只列出前20个文档。
遍历完游标做哦你那个所有的文档之后，或者10分钟后，游标会自动关闭，可以使用noCursorTimeout()函数保持游标一直有效

var myCursor = db.accounts.find();
myCursor[1]

不遍历，游标一直存在。遍历完，游标关闭
var myCursor = db.accounts.find().noCursorTimeout();
关闭游标
myCursor.close();

游标函数
	cursor.hasNext()
	cursor.next()

遍历游标
	var myCursor = db.accounts.find({name: "b33"});
	while(myCursor.hasNext()){
		printjson(myCursor.next());
	}

forEach遍历
	var myCursor = db.accounts.find({name: "b33"});
	myCursor.forEach(elem => printjson(elem))

	db.accounts.find({name: "b33"}).limit(1);
limit(0)相当于不使用limit
	db.accounts.find({name: "b33"}).limit(0);

db.accounts.find({name: "b33"}).skip(1);


db.accounts.find({name: "b33"}).limit(1).count();
	返回结果是4，count()默认不考虑 cursor.limit()、cursor.skip()的效果

db.accounts.find({name: "b33"}).limit(1).count(true);
	count(true)会考虑limit()、skip()的效果

skip() 会在 limit() 之前执行


文档投影，只返回部分字段。返回name字段
	db.accounts.find({},{name: 1})
连 _id 也不返回
	db.accounts.find({},{name: 1, _id:0})
不返回name、_id
	db.accounts.find({},{name: 0, _id:0})
除了_id，其他字段不能混用包含于不包含
	db.accounts.find({},{balance:1, name: 0, _id:0})

切割数组，报错了
	db.accounts.find({},{name: 1, _id:0, contact: {$slide: 1}})

$elemMatch返回数组字段中满足筛选条件的第一个元素
db.accounts.find({},{_id: 0, name: 1, contact: {$elemMatch: {$gt: "Alabama"}}})

更新文档
db.<collection>.update(<query>, <update>, <options>)
<query>文档定义了更新操作时筛选文档的条件
<update>文档提供了更新的内容
<options>文档声明更新操作的选项
如果<update>文档不包含任何更新操作符，db.collection.update()将会使用<update>文档直接替换集合中符合<query>文档筛选条件的文档
db.accounts.find({name: "alice"})

更新操作，_id不会被更新
	db.accounts.update({name: "alice"}, {name: "alice", balance: 1})
不建议在update中使用_id
	db.accounts.update({name: "alice"}, {_id: "account1", name: "alice", balance: 2})

db.accounts.find({balance: {$gt: 2, $lt: 2000}}) 有两条记录

如果使用update文档替换整篇文档时，只有第一篇文档会被更新
结果只有第一个文档被更新了
	db.accounts.update({balance: {$gt: 2, $lt: 2000}}, {name: "alice", balance: 100, add: "新增字段"})

更新特定字段
	文档更新操作符
		$set 更新、新增字段
		$unset 删除字段
		$rename 重命名字段
		$inc 加
		$mul 减

db.accounts.find({name: "jack"}).pretty()

$set新增一个字段
	db.accounts.update({name: "jack"}, 
	{$set:
		{
			balance: 100,
			info: {
				dateOpened: new Date("2016-05-18T16:00:00Z"),
				branch: "branch1"
			}
		}
	})

$set更新字段
	db.accounts.update({name: "jack"}, 
		{$set:
			{
				"info.dateOpened": new Date("2017-05-18T16:00:00Z")
			}
		})
$set更新数组
	db.accounts.update({name: "jack"}, 
		{$set:
			{
				"contact.0": "666"
			}
		})
$set更新数组，新增元素，中间下标填充null
	db.accounts.update({name: "jack"}, 
		{$set:
			{
				"contact.5": "666"
			}
		})
$unset删除数组元素，长度不变，只是元素被设置为null
	db.accounts.update({name: "jack"}, 
		{$unset:
			{
				"contact.0": ""
			}
		})
$rename重命名字段，如果字段不存在，文档不会被更新
	db.accounts.update({name: "jack"}, 
		{$rename:
			{
				"notExit": "name"
			}
		})

更新数组
	$addToSet  向数组中添加元素
	$pop    从数组中移除元素
	$pull  选择性地移除元素
	$pullAll   选择性地移除元素
	$push   添加元素

$addToSet元素在数组中已经存在，不会新增
	db.accounts.update({name: "jack"}, 
		{$addToSet:
			{
				"contact": "Alabama"
			}
		})
	数组添加元素
	db.accounts.update({name: "jack"}, 
		{$addToSet:
			{
				"contact": ["Alabama", "dasdfa"]
			}
		})	
	$addToSet和$each组合，数组元素添加到数组
	db.accounts.update({name: "jack"}, 
		{$addToSet:
			{
				"contact": {$each: ["Alabama", "dasdfa"]}
			}
		})
	删除数组元素
	db.accounts.update({name: "jack"}, 
		{
			$pop: {contact: 1}
		})

	<options> multi: true 更新多篇文档。mongodb只能保证单个文档修改的原子性，不能保证多个文档更新的原子性
	db.accounts.update({name: "jack"}, 
		{
			$set: {currency: 1}
		},{
			multi: true
		})

删除文档
	db.<collection>.remove(<query>, <options>)
		<query>     筛选条件
		<options>   删除操作的参数
删除文档，remove跟update默认只修改一篇文档不一样，remove默认会删除满足筛选条件的所有文档
	db.accounts.remove({balance: 100})

删除集合
	db.<collection>.drop()

聚合操作
	db.<collection>.aggregate(<pipeline>, <options>)
		<pipeline> 定义使用的聚合管道阶段和聚合操作符
		<options> 操作参数
聚合表达式
	$<field>  用$来指示字段路径
	$<field>.<sub-field> 使用$和.表示内嵌字段路径
使用$$指示系统变量，$$CURRENT指示当前操作的文档


db.accounts.insertMany([
	{
		name: {firstName: "alice", lastName: "wong"},
		balance: 50
	},{
		name: {firstName: "bob", lastName: "yang"},
		balance: 20
	}
])

$project对文档重新投影，可以灵活控制文档输出格式
_id 不输出，balance 输出，$name.firstName重命名为clientName
	db.accounts.aggregate([{
		$project: {
			_id: 0,
			balance: 1,
			clientName: "$name.firstName"
		}
	}])

$match 筛选，跟CRUD的筛选语法相同
	db.accounts.aggregate([{
		$match: {
			"name.firstName": "alice"
		}
	}])
$or条件
	db.accounts.aggregate([{
		$match: {
			$or: [
				{"name.firstName": "alice"},
				{"name.lastName": "yang"},
			]
		}
	}])
两个聚合阶段一起使用
	db.accounts.aggregate([{
		$match: {
			$or: [
				{"name.firstName": "alice"},
				{"name.lastName": "yang"},
			]
		}
	},{
		$project: {
			_id: 0
		}
	}])



db.accounts.update({
	"name.firstName": "alice"
},{
	$set: {currency: ["CNY", "USD"]}
})

db.accounts.update({
	"name.firstName": "bob"
},{
	$set: {currency: "GBP"}
})

会把数组展开成元素，生成新文档，文档的其他部分不变。查询结果增多
db.accounts.aggregate([{
	$unwind: {
		path: "$currency"
	}
}])

排序，balance从小到大，name.lastName从大到小
db.accounts.aggregate([{
	$sort: {balance: 1, "name.lastName": -1}
}])


db.forex.insertMany([{
		ccy: "USD",
		rate: 6.91,
		date: new Date(2018-12-21)
	},{
		ccy: "GBP",
		rate: 8.72,
		date: new Date(2018-08-21)
	},{
		ccy: "CNY",
		rate: 1.0,
		date: new Date(2018-12-21)
}])


使用$lookup对单一字段值进行查询
	$lookup: {
		from: 同一个数据库中的其他集合
		localField: 管道中用于查询的字段
		foreignField: from中的字段
		as: 新插入字段的名字
	}
accounts的currency中包含forex.ccy的值，forex的文档会加入到forexData字段
	db.accounts.aggregate([{
		$lookup: {
			from: "forex",
			localField: "currency",
			foreignField: "ccy",
			as: "forexData"
		}
	}])

$unwind会将currency没有值的文档过滤掉，在执行$lookup，可以减少文档数
	db.accounts.aggregate([{
		$unwind: {
			path: "$currency"
		}
	},{
		$lookup: {
			from: "forex",
			localField: "currency",
			foreignField: "ccy",
			as: "forexData"
		}
	}])


$lookup: {
	from: 同一个数据库中的其他集合
	let: 如果pipeline需要原管道文档的字段，使用let对字段进行声明。let本身就是声明变量的意思
	pipeline: 对查询集合中的文档使用聚合阶段处理
	as: 新插入字段的名字
}
pipeline查询条件和管道文档之间没有直接关联，称为不相关查询，$lookup从3.6版本开始支持不相关查询
	db.accounts.aggregate([{
		$lookup: {
			from: "forex",
			pipeline: [{
				$match: {
					date: new Date(2018-12-21)
				}
			}],
			as: "forexData"
		}
	}])

使用let声明bal变量，使用$expr结合$$bal获取系统变量
	db.accounts.aggregate([{
		$lookup: {
			from: "forex",
			let: {bal: "$balance"},
			pipeline: [{
				$match: {
					$expr: {
						$and: [
							{$eq: ["$date", new Date(2018-12-21)]},
							{$gt: ["$$bal", 20]}
						]
					}
				}
			}],
			as: "forexData"
		}
	}]).pretty()

聚合
$group:{
	_id: 分组字段,
	<field>: 分组之后再进行其他操作，例如求和，使用聚合操作符重新定义字段
}

db.transactions.insertMany([{
	symbol: "600519",
	qty: 100,
	price: 567.4,
	currency: "CNY"
},{
	symbol: "AMZN",
	qty: 1,
	price: 1377.5,
	currency: "USD"
},{
	symbol: "AAPL",
	qty: 2,
	price: 150.7,
	currency: "USD"
}])

使用currency分组，
	db.transactions.aggregate([{
		$group:{
			_id: "$currency"
		}
	}])
	输出结果是：
		{ "_id" : "USD" }
		{ "_id" : "CNY" }

分组后对qty求和并赋值给totalQty
$multiply乘法
count: {$sum: 1}，有一篇文档就加1，分组中文档的数量
$max分组中最大值
$min分组中最小值
	db.transactions.aggregate([{
		$group: {
			_id: "$currency",
			totalQty: { $sum: "$qty"},
			totalNotional: { $sum: { $multiply: ["$price", "$qty"] } },
			avgPrice: {$avg: "$price"},
			count: {$sum: 1},
			maxNotional: {$max: {$multiply: ["$price", "$qty"]}},
			minNotional: {$min: {$multiply: ["$price", "$qty"]}}
		}
	}])

使用聚合操作符创建数组字段
	db.transactions.aggregate([{
		$group: {
			_id: "$currency",
			symbols: {$push: "$symbol"}
		}
	}])

$out将聚合管道中的文档写入一个新集合
	db.transactions.aggregate([{
		$group: {
			_id: "$currency",
			symbols: {$push: "$symbol"}
		}
	},{
		$out: "output"
	}])

	db.output.find()

管道文档写入一个已经存在的文档，会清除掉output原有的文档
	db.transactions.aggregate([{
		$group: {
			_id: "$symbol",
			totalNotional: { $sum: { $multiply: ["$price", "$qty"] } },
		}
	},{
		$out: "output"
	}])

	db.output.find()

db.<collection>.aggregate(<pipeline>, <options>)
	options参数
		allowDiskUse: <boolean>
		每个聚合管道阶段使用的内存不能超过100MB。allowDiskUse=true 内存不足时，将数据写入临时文件中，临时文件默认值为/data/db/_tmp

	db.transactions.aggregate([{
		$group: {
			_id: "$symbol",
			symbols: {$push: "$symbol"}
		}
	}],{
		allowDiskUse: true
	})

mongoDB默认的优化
	$match尽可能地在$project之前执行，如果$match使用了$project定义的新字段，$match就没法放在$project阶段了
	$match在$sort之前
	$skip在$project

mongoDB索引也是使用B-tree
复合键索引可以对多个字段排序
索引值是文档地址


创建索引
	db.<collection>.createIndex(<keys>, <options>)
		keys: 创建索引的字段

db.accountsWithIndex.insertMany([{
	name: "alice", balance: 50, currency: ["GBP", "USD"]
},{
	name: "bob", balance: 20, currency: ["AUD", "USD"]
},{
	name: "bob", balance: 300, currency: ["CNY"]
}])

创建索引，1表示索引从小到大排列
	db.accountsWithIndex.createIndex({name: 1})
查看已经存在的索引
	db.accountsWithIndex.getIndexes()
创建复合索引
	db.accountsWithIndex.createIndex({name: 1, balance: -1})
多键索引，给数组字段使用
	db.accountsWithIndex.createIndex({currency: 1})

查看索引效果
	db.<collection>.explain().<method(..)>
		使用explain()可以分析的命令包括aggregate()、count()、distinct()、find()、group()、remove()、update()

db.accountsWithIndex.explain().find({balance: 100})
	查看winningPlan，"stage" : "COLLSCAN" COLLSCAN是Collection scan的缩写，扫描整个集合

db.accountsWithIndex.explain().find({name: "alice"})
	winningPlan，"stage" : "IXSCAN" Index scan 索引扫描
	"stage" : "FETCH", 通过索引的值（文档地址）去找文档

db.accountsWithIndex.explain().find({name: "alice"}, {_id:0, name: 1})
	winningPlan 的 "stage" : "FETCH" 也不见了，只返回name,直接在索引中取数据

db.accountsWithIndex.explain().find().sort({name: 1, balance: 1})
	winningPlan 的 "stage" : "SORT" 把所有文档提取出来，然后排序，效率不高

删除索引，没有更新索引的方法，先删除再建立
	db.accountsWithIndex.dropIndex()

获取索引
	db.accountsWithIndex.getIndexes()
通过索引名字删除索引
	db.accountsWithIndex.dropIndex("name_1")

唯一索引
	db.accountsWithIndex.createIndex({balance: 1}, {unique: true})

有唯一索引的文档，只有一篇文档可以设置balance为null，后面的文档balance为null，则没法插入


索引的稀疏性
	只将包含索引键字段的文档加入到索引中，即索引键字段值为null
		db.accountsWithIndex.createIndex({balance: 1}, {sparse: true})
	如果一个索引既具备唯一性，又具备稀疏性，就可以保存多篇缺失索引键值的文档
		db.accountsWithIndex.dropIndex("balance_1")
		db.accountsWithIndex.createIndex({balance: 1}, {unique: true, sparse: true})

可以插入多个缺少balance的文档
	db.accountsWithIndex.insert({name: "david", lastAccess: new Date()})

索引有效期，设定了生存时间的索引，会自动删除字段值超过生存时间的文档
生存时间只能设置给单键索引，数组则使用元素最小值
	db.accountsWithIndex.find()

lastAccess与当前时间比较，时间间隔大于20秒，删除文档。是通过一个后台线程删除
	db.accountsWithIndex.createIndex({lastAccess: 1}, {expireAfterSeconds: 20})


没有固定的数据格式 != 无需设计数据模型


树形结构文档，使用parent_id
	{
		id: "Document"
		parent: null
	}
	{
		id: "MongoDB"
		parent: "Document"
	}
也可以这样，children存储子节点id
	{
		"id": "Database",
		"children": ["Relational", "Non-relational"]
	}
	{
		"id": "Relational",
		"children": ["Document"]
	}
还可以把父级全部存储到ancestors中
	{
		"id": "MongoDB",
		"ancestors": ["Relational", "Document"]
	}


复制集（数据复制，多个节点）
	每个节点都会向其他节点发送

主节点负责处理所有写入请求
主节点和副节点都可以处理读取请求
副节点从主节点（或者符合条件的副节点）处复制数据

复制集每个节点都会想其他节点发送心跳请求
每隔2秒发送一次，超过10秒则请求超时（默认）
复制集中最多可以有50个节点

复制集选举
	候选节点发起选举，每个节点投票给比自己更同步的节点
	得到超过半数选票的候选节点会当选主节点
	复制集最多可以有7个投票节点

初始化的时候同步数据：数据库、集合、索引、文档
	后面新增的数据，会有写库记录（主节点接收的请求），备节点批量拷贝写库记录，将记录存储在local.oplog.rs中，备节点执行写库记录
		写库记录可以被重复使用
		多个线程分批次使用日志记录，比如按主键分类，一个线程执行一个主键分类的记录

创建复制集
先创建网络
docker network create mongonetwork
docker network ls

mkdir -p /mymongo/data1

// --replSet myset 指定复制集的名字
docker run --net mongonetwork --name mongo1 -v /mymongo/data1:/data/db -p 27017:27017 -d mongo:4.2 --replSet myset --port 27017

// 第二个复制集， --port 27018修改docker中mongoDB的端口
docker run --net mongonetwork --name mongo2 -v /mymongo/data2:/data/db -p 27018:27018 -d mongo:4.2 --replSet myset --port 27018

docker run --net mongonetwork --name mongo3 -v /mymongo/data3:/data/db -p 27019:27019 -d mongo:4.2 --replSet myset --port 27019

复制集初始化
docker exec -it mongo1 mongo

// myset是复制集的名字，3个节点都在同一个docker网络下，host可以使用容器的名字:端口
	rs.initiate({
		_id: "myset",
		members: [
			{_id: 0, host: "192.168.3.178:27017"},
			{_id: 1, host: "192.168.3.178:27018"},
			{_id: 2, host: "192.168.3.178:27019"}
		]
	})
查看复制集状态
	rs.status()

分片
	可以把数据库的数据分成几份，存储在不同的服务器中

分片集群：
	每个分片存储一部分数据，可以部署为复制集
	配置服务器，保存集群配置和元数据，可以部署为复制集
	mongos，这是一个路由，请求到达mongos，mongos先去询问配置服务器，得到数据在哪个分片上，然后再去分片查找数据。

集群中的每个数据库都会选择一个分片作为主分片
主分片存储所有不需要分片的集合
创建数据库时，数据最少的分片被选为主分片

分片片键
	{x: 7, y: "abc", z: true}
	比方说取x字段作为分片片键，hash(x)之后取模

片键值被用来作为集合中的文档划分为数据段
片键必须对应一个索引或者索引前缀（单键或者复合键）
可以使用片键值的哈希值来生成哈希片键	

片键值范围广，必须boolean类型作为片键就不好，可使用符合片键扩大范围
片键值的分布更平衡
片键值不要单向的增大/减少（可使用哈希片键）


配置服务器，可以使用复制集
	储存各分片数据段列表和数据段范围
	存储集群的认证和授权配置
	不同的集群不要共用配置服务器

配置服务器使用复制集
	如果主节点故障，配置服务器进入只读模式
	只读模式下，数据段分裂和集群平衡不可执行（数据段分裂可以使数据分布更均匀）
	整个复制集故障，分片集群不可用


mongos分片查询
	查询请求是分片片键，使用分片片键就知道数据在哪个分片
	查询请求不是分片片键，mongos通过配置服务器也不知道数据在哪个分片，所以会将请求发送给所有分片


启用身份认证
	docker run --name mymongo -v /mymongo/data:/data/db -d mongo:4.2 --auth

设置账号密码
	use admin
	db.createUser({ user: 'root', pwd: 'poly2017', roles: [ { role: "userAdminAnyDatabase", db: "admin" } ] });	

删除docker容器
	docker stop mymongo && docker rm $_

使用用户名、密码进行身份认证登陆
	docker exec -it mymongo bash 
	mongo -u "root" -p "poly2017" --authenticationDatabase "admin";

	最终root只有用户管理的权限，没有数据权限

使用db.auth()进行身份认证，相当于登陆
在docker容器中，执行
	进入mongoDB客户端
		mongo
	使用admin数据库
		use admin
	认证，相当于登陆	
		db.auth("root", "poly2017")

权限
权限 = 在哪里 + 做什么
	{resource: {db: "test", collection: ""}, actions: ["find", "update"]}
	{resource: {cluster: true}, actions: ["shutdown"]}

角色
	read      - 读取当前数据库中所有非系统集合
	readWrite - 读写当前数据库中所有非系统集合
	dbAdmin   - 管理当前数据库
	userAdmin - 管理当前数据库中的用户和角色

readAnyDatabase、readWriteAnyDatabase、dbAdminAnyDatabase、userAdminAnyDatabase
对所有数据库执行操作，只在admin数据库提供

db.createUser({ user: 'root1', pwd: 'poly2017', roles: [ { role: "userAdminAnyDatabase", db: "admin" },{ role: "readWriteAnyDatabase", db: "admin" } ] });

数据库不存在，则创建数据库，否则切换到指定数据库。
	use app 

非 admin 库，不能拥有 clusterAdmin、readAnyDatabase、readWriteAnyDatabase、userAdminAnyDatabase、dbAdminAnyDatabase 这些角色。
可授予多个角色
	db.createUser({
		user: "root",
		pwd: "poly2017",
		roles: [{
			role: "readWriteAnyDatabase",
			db: "admin"
		},{
			role: "userAdminAnyDatabase",
			db: "admin"
		}] 
	})

授权之后要退出mongo客户端
	exit
重新登录客户端
	mongo
先使用admin数据库
	use admin
在admin数据库认证用户
	db.auth("user02", "poly2017")

也可以docker容器中直接使用这一句代码
	mongo -u "root" -p "poly2017" --authenticationDatabase "admin"


创建自定义角色，角色只在test数据库accounts集合中有效，roles继承某些角色

	use test;
	db.createRole({
		role: "readAccounts",
		privileges: [{
			resource: {db: "test", collection: "accounts"},
			actions: ["find"]
		}],
		roles: []
	})


数据处理工具
	mongoexport
	mongoimport

mongoexport 将数据导出为json或者csv格式文件

在docker容器中，执行
	mongoexport --db test --collection accounts --type=csv --fields name,balance --out /opt/backups/accounts.csv -u root -p poly2017 --authenticationDatabase admin














