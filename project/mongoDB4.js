装mongoDB
	docker pull mongo:4
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







