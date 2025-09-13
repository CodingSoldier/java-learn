package org.cpq;

import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.code.DataType;
import com.serotonin.modbus4j.exception.ErrorResponseException;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.ip.IpParameters;
import com.serotonin.modbus4j.locator.BaseLocator;
import com.serotonin.modbus4j.msg.ReadHoldingRegistersRequest;
import com.serotonin.modbus4j.msg.ReadHoldingRegistersResponse;
import com.serotonin.modbus4j.msg.WriteRegisterRequest;
import com.serotonin.modbus4j.msg.WriteRegisterResponse;

public class A01modbus4j {
    public static void main(String[] args) {
        // 创建 Modbus 工厂
        ModbusFactory factory = new ModbusFactory();

        // 配置 TCP 参数
        IpParameters params = new IpParameters();
        params.setHost("127.0.0.1"); // 设备 IP
        params.setPort(502);           // 默认端口 502

        // 创建 Modbus TCP 主站
        ModbusMaster master = factory.createTcpMaster(params, true); // false 表示非长连接

        // 示例 1: 读取保持寄存器（功能码 03）
        int slaveId = 1;     // 从站地址
        int startOffset = 0; // 寄存器起始地址
        int numberOfRegisters = 10; // 读取数量

        // 初始化连接
        try {
            master.init();
        } catch (ModbusInitException e) {
            e.printStackTrace();
            System.out.println("master.init()发生了异常");
            return;
        }

        try {
            holdingRegisterInt(slaveId, startOffset, numberOfRegisters, master);

            // holdingRegisterFloat(slaveId, master);

            // 示例 2: 写入单个寄存器（功能码 06）
            // writeRegister(slaveId, 0, 1234, master);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            master.destroy(); // 关闭连接
        }
    }

    /**
     * 读取整型数据
     * 图片：A01modbus4j-读取int数据.jpg
     */
    private static void holdingRegisterInt(int slaveId, int startOffset, int numberOfRegisters, ModbusMaster master) throws ModbusTransportException, ErrorResponseException {
        ReadHoldingRegistersRequest request = new ReadHoldingRegistersRequest(slaveId, startOffset, numberOfRegisters);
        ReadHoldingRegistersResponse response = (ReadHoldingRegistersResponse) master.send(request);
        if (!response.isException()) {
            short[] values = response.getShortData();
            System.out.println("寄存器值0：" + values[0] );
            System.out.println("寄存器值1：" + values[1] );
            System.out.println("寄存器值2：" + values[2] );
            System.out.println("寄存器值3：" + values[3] );
        }

        // DataType.TWO_BYTE_INT_SIGNED 是 2字节有符号整数
        BaseLocator<Number> loc = BaseLocator.holdingRegister(slaveId, 1, DataType.TWO_BYTE_INT_SIGNED);
        Number value = master.getValue(loc);
        System.out.println("读取到的值：" + value);
    }

    /**
     * 读取整型数据
     * 图片：A01modbus4j-读取Float数据.jpg
     */
    private static void holdingRegisterFloat(int slaveId, ModbusMaster master) throws ModbusTransportException, ErrorResponseException {
        // DataType.FOUR_BYTE_FLOAT 是4字节浮点数
        BaseLocator<Number> num7 = BaseLocator.holdingRegister(slaveId, 7, DataType.FOUR_BYTE_FLOAT);
        Number v7 = master.getValue(num7);
        System.out.println("读取到的值v7：" + v7);
    }

    /**
     * 写入单个寄存器
     * 图片：A01modbus4j-写单个寄存器.jpg
     */
    private static void writeRegister(int slaveId, int writeOffset, int valueToWrite, ModbusMaster master) throws ModbusTransportException {
        WriteRegisterRequest writeRequest = new WriteRegisterRequest(slaveId, writeOffset, valueToWrite);
        WriteRegisterResponse writeResponse = (WriteRegisterResponse) master.send(writeRequest);
        if (!writeResponse.isException()) {
            System.out.println("写入成功");
        }
    }

}