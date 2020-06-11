安装mongoDB
	docker pull mongo:4
	mkdir -p /mymongo/data
	docker run --restart always --name mymongo -p 27017:27017 -v /mymongo/data:/data/db -d mongo:4

安装MongoDB管理界面 Mongo Express
	docker pull mongo-express
	docker run  --restart always --link mymongo:mongo -p 8081:8081 -d mongo-express

	访问  http://192.168.3.178:8081/
	默认有admin、config、local 3个数据库，不建议使用默认的数据库

进入mongo shell   
	docker exec -it mymongo mongo
	print('hello')   使用js打印hello
	exit    退出

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
		_id: "account1",
		name: "alice11",
		balance: 100
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
			balance: 2000,
			contact: ["1111", "Alabama", "US"]
		},
		{
			name: "karen",
			balance: 2500,
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









