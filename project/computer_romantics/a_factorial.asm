# ��ʼ�ݹ麯������
addiu $sp, $0, 0x10010080 

# ѹջ���
addiu $s0, $0, 5 # n=5
sw $s0, 0($sp)
addiu $sp, $sp, -4

jal FACT
nop
j END
nop

FACT:

# ѹջ���ص�ַ
sw $ra, 0($sp)
addiu $sp, $sp, -4

#��ȡ���
lw $s0, 8($sp)

#ѹջ����ֵ
sw $0, 0($sp)
addiu $sp, $sp, -4

#�ݹ�base����
# if (n == 0) { return 1}
bne $s0, $0, RECURSION
nop
# ��ȡ�·��ص�ַ
lw $t1, 8($sp)
# ��ջ������ֵ�����ص�ַ
addiu $sp, $sp, 8
# ѹջ����ֵ
addiu $s0, $zero, 1
sw $s0, 0($sp)
addiu $sp, $sp, -4

jr $t1
nop

RECURSION : # recursion
# return fact(n-1) * n

#ѹջ����
addiu $s1, $s0, -1
sw $s1, 0($sp)
addiu $sp, $sp, -4

jal FACT
nop

# ���ڵ�ջ��ʲô���ӵģ� ���� | ���ص�ַ | ����ֵ | �Ӻ����Ĳ��� | �Ӻ����ķ���ֵ | ��ǰSP
# ��ǰ����
lw $s0, 20($sp)
# �Ӻ�������ֵ
lw $s1, 4($sp)
# ���ص�ַ
lw $t1, 16($sp)

mult $s1, $s0
mflo $s2

# ��ջ��
addiu $sp, $sp, 16

# ����ֵѹջ
sw $s2, 0($sp)
addiu $sp, $sp, -4

jr $t1
nop

END: