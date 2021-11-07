package com.datastructure_arithmetic_2;


import java.util.Random;

public class MyBloomFilter<T> {
	/**
	 * 二进制向量的长度(一共有多少个二进制位)
	 */
	private int bitSize;
	/**
	 * 二进制向量
	 */
	private long[] bits;
	/**
	 * 哈希函数的个数
	 */
	private int hashSize;
	
	/**
	 * @param n 数据规模
	 * @param p 误判率, 取值范围(0, 1)
	 */
	public MyBloomFilter(int n, double p) {
		if (n <= 0 || p <= 0 || p >= 1) {
			throw new IllegalArgumentException("wrong n or p");
		}
		
		double ln2 = Math.log(2);
		// 求出二进制向量的长度
		bitSize = (int) (- (n * Math.log(p)) / (ln2 * ln2));
		// 求出哈希函数的个数
		hashSize = (int) (bitSize * ln2 / n);
		// bits数组的长度
		// int size = (bitSize + Long.SIZE - 1) / Long.SIZE;
		int size = (int) Math.ceil(bitSize / Long.SIZE);
		bits = new long[size];
		// 每一页显示100条数据, pageSize
		// 一共有999999条数据, n
		// 请问有多少页 pageCount = (n + pageSize - 1) / pageSize
	}
	
	/**
	 * 添加元素1
	 */
	public boolean put(T value) {
		nullCheck(value);

		// 利用value生成2个整数
		int hash1 = value.hashCode();
		int hash2 = hash1 >>> 16;
	
		boolean result = false;
		for (int i = 1; i <= hashSize; i++) {
			int combinedHash = hash1 + (i * hash2);
			if (combinedHash < 0) {
				combinedHash = ~combinedHash;
			} 
			// 生成一个二进位的索引
			int index = combinedHash % bitSize;
			// 设置index位置的二进位为1
			if (set(index)) result = true;
			
			//   101010101010010101
			// | 000000000000000100   1 << index
			//   101010111010010101
		}
		return result;
	}
	
	/**
	 * 判断一个元素是否存在
	 */
	public boolean contains(T value) {
		nullCheck(value);
		// 利用value生成2个整数
		int hash1 = value.hashCode();
		int hash2 = hash1 >>> 16;
	
		for (int i = 1; i <= hashSize; i++) {
			int combinedHash = hash1 + (i * hash2);
			if (combinedHash < 0) {
				combinedHash = ~combinedHash;
			} 
			// 生成一个二进位的索引
			int index = combinedHash % bitSize;
			// 查询index位置的二进位是否为0
			if (!get(index)) return false;
		}
		return true;
	}
	
	/**
	 * 设置index位置的二进位为1
	 */
	private boolean set(int index) {
		int i = index / Long.SIZE;
		long value = bits[i];

		/**
		 * 添加新元素取hash后是index
		 * 1 << (index % Long.SIZE) 的意思是：
		 * 假如 index = 5，index % Long.SIZE = 5
		 * 左移5位，即变成 000XXXXXX0100000，前面是N个0
		 */
		int bitValue = 1 << (index % Long.SIZE);

		/**
		 * 先取出原来的元素，原来的元素与新元素做或操作
		 * 两个整数的或操作是将两个整数变成二进制，然后做与操作
		 *
		 * 例如：
		 * index / Long.SIZE = 5
		 * bits[index / Long.SIZE] = 9
		 * 9转二进制是xxxX00001001，bitValue用二进制表示是XXXXXX0000100000
		 * xxxX00001001 | XXXXXX0000100000 = XXXXXX0000101001，转为十进制是41
		 * bits[index / Long.SIZE] = 41
		 * 同时41转二进制后也可以表示哪些位是1，哪些位是0
		 */
		bits[index / Long.SIZE] = value | bitValue;
		return (value & bitValue) == 0;
	}
	
	/**
	 * 查看index位置的二进位的值
	 * @return true代表1, false代表0
	 */
	private boolean get(int index) {
		long value = bits[index / Long.SIZE];
		return (value & (1 << (index % Long.SIZE))) != 0;
	}
	
	private void nullCheck(T value) {
		if (value == null) {
			throw new IllegalArgumentException("Value must not be null.");
		}
	}

	public static void main(String[] args) {
		MyBloomFilter<Integer> bloomFilter = new MyBloomFilter<>(1_0000_0000, 0.00001);
		for (int i = 0; i < 1000; i++) {
			bloomFilter.put(new Random().nextInt()/3);
		}

		bloomFilter.put(10);

		boolean c = bloomFilter.contains(10021);
		System.out.println(c);


		// BloomFilter<Integer> filter = BloomFilter.create(
		// 		Funnels.integerFunnel(),
		// 		500,
		// 		0.01);

	}
}
