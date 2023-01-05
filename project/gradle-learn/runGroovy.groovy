/**
 * 可选的类型定义
 */
def version = 1

/**
 * assert语句
 */
assert version == 1

/**
 * 括号可选。
 */
println(version)
println version

/**
 * 字符串
 */
def s1 = '单引号中就纯粹的字符串'
def s2 = "双引号中通过\$后取变量${version}"
def s3 = '''三个单引号
可以
换行'''
println s1
println s2
println s3
