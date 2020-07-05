package pattern.factory.abstractf;

import pattern.factory.Keyboard;
import pattern.factory.Mouse;

public interface ComputerFactory {
	/**
	 * 抽象工厂模式：提供一个创建一系列相关或者相互依赖对象的接口
	 * 		抽象工厂模式侧重的是同一个产品族
	 * 		工厂方法模式更侧重于同一个产品等级
	 *
	 * 抽象工厂解决了只支持生产一种产品的弊端
	 * 新增一个产品族，只需要新增一个新的具体工厂，不需要修改原来的代码，依旧满足开闭原则
	 *
	 * 但是添加新产品时，例如添加主板，依旧违背开闭原则，增加系统复杂度
	 *
	 */

	Mouse createMouse();

	Keyboard createKeyboard();

}
