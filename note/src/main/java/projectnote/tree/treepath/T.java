package projectnote.tree.treepath;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import projectnote.tree.treepath.mapper.TreepathExpandMapper;
import com.utils.BaseTest;
import com.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author：陈丕迁
 * @Description：
 * @Date： 2017/12/9
 */
public class T extends BaseTest{
    @Autowired
    TreepathExpandMapper mapper;

    @Test
    public void test1() throws Exception{
        List<TreepathExpand> qList = mapper.queryByPath("0001");

        List<TreepathExpand> rList = new ArrayList<TreepathExpand>();

        for(TreepathExpand expand:qList){
            if(expand.getParentId() == null){
                rList.add(expand);
            }
        }

        for(TreepathExpand expand: rList){
           expand.setChildrenList(recursion(expand.getId(),qList));
        }

        System.out.println(rList);

    }

    public List<TreepathExpand> recursion(int id, List<TreepathExpand> expands) throws Exception{

        List<TreepathExpand> list = new ArrayList<TreepathExpand>();
        for(TreepathExpand expand:expands){
            if( Utils.numValEqual(id, expand.getParentId())){
                list.add(expand);
            }
        }
        for(TreepathExpand expand:list){
            expand.setChildrenList(recursion(expand.getId(), expands));
        }

        return list;
    }







    @Test
    public void t2() throws Exception{
        String str = "[{\"isbusinessprocess\":\"Y\",\"isorgviewflag\":\"Y\",\"businessproperty\":\"2\",\"businessdesc\":\"\",\"extfield2\":\"\",\"extfield3\":\"\",\"extfield7\":\"\",\"extfield1\":\"\",\"extfield5\":\"\",\"extfield4\":\"\",\"extfield6\":\"\",\"extfield8\":\"\",\"businesscode\":\"IDB_SYS_MANAGEMENT\",\"isleaf\":\"N\",\"treecode\":\"0002\",\"sortorder\":1,\"parentbusinessid\":\"\",\"businessname\":\"内部管理\",\"modifytime\":1514970067000,\"businessid\":\"0000015\",\"isvalid\":\"Y\",\"fullcode\":\"neibuguanli\",\"shortcode\":\"bnbgl\"},{\"isbusinessprocess\":\"Y\",\"isorgviewflag\":\"Y\",\"businessproperty\":\"2\",\"businessdesc\":\"\",\"extfield2\":\"\",\"extfield3\":\"\",\"extfield7\":\"\",\"extfield1\":\"\",\"extfield5\":\"\",\"extfield4\":\"\",\"extfield6\":\"\",\"extfield8\":\"\",\"businesscode\":\"IDB_USER_CONFIRM\",\"isleaf\":\"Y\",\"treecode\":\"00020001\",\"sortorder\":0,\"parentbusinessid\":\"0000015\",\"businessname\":\"用户身份验证\",\"modifytime\":1510632372000,\"businessid\":\"0000016\",\"isvalid\":\"Y\",\"fullcode\":\"yonghushenfenyanz\",\"shortcode\":\"yhsfyz\"},{\"isbusinessprocess\":\"Y\",\"isorgviewflag\":\"Y\",\"businessproperty\":\"2\",\"businessdesc\":\"\",\"extfield2\":\"\",\"extfield3\":\"\",\"extfield7\":\"\",\"extfield1\":\"\",\"extfield5\":\"\",\"extfield4\":\"\",\"extfield6\":\"\",\"extfield8\":\"\",\"businesscode\":\"IDB_OPRATION_SUPPORT\",\"isleaf\":\"N\",\"treecode\":\"0001\",\"sortorder\":0,\"parentbusinessid\":\"\",\"businessname\":\"运营支撑类业务\",\"modifytime\":1510027572000,\"businessid\":\"0000067\",\"isvalid\":\"Y\",\"fullcode\":\"yunyingzhichengleiyewu\",\"shortcode\":\"yyzclyw\"},{\"isbusinessprocess\":\"Y\",\"isorgviewflag\":\"Y\",\"businessproperty\":\"2\",\"businessdesc\":\"\",\"extfield2\":\" \",\"extfield3\":\" \",\"extfield7\":\" \",\"extfield1\":\" \",\"extfield5\":\" \",\"extfield4\":\" \",\"extfield6\":\" \",\"extfield8\":\" \",\"businesscode\":\"IDB_DEVICE_REPAIR\",\"isleaf\":\"Y\",\"treecode\":\"00010001\",\"sortorder\":0,\"parentbusinessid\":\"0000067\",\"businessname\":\"设备故障维修\",\"modifytime\":1510113972000,\"businessid\":\"0000068\",\"isvalid\":\"Y\",\"fullcode\":\"shebeiguzhangweixiu\",\"shortcode\":\"sbgzwx\"},{\"isbusinessprocess\":\"Y\",\"isorgviewflag\":\"Y\",\"businessproperty\":\"2\",\"businessdesc\":\"\",\"extfield2\":\"\",\"extfield3\":\"\",\"extfield7\":\"\",\"extfield1\":\"\",\"extfield5\":\"\",\"extfield4\":\"\",\"extfield6\":\"\",\"extfield8\":\"\",\"businesscode\":\"IDB_DEVICE_INSTALL\",\"isleaf\":\"Y\",\"treecode\":\"00010002\",\"sortorder\":1,\"parentbusinessid\":\"0000067\",\"businessname\":\"设备安装\",\"modifytime\":1510200372000,\"businessid\":\"0000069\",\"isvalid\":\"Y\",\"fullcode\":\"shebeianzhuang\",\"shortcode\":\"sbaz\"},{\"isbusinessprocess\":\"Y\",\"isorgviewflag\":\"Y\",\"businessproperty\":\"2\",\"businessdesc\":\"\",\"extfield2\":\"\",\"extfield3\":\"\",\"extfield7\":\"\",\"extfield1\":\"\",\"extfield5\":\"\",\"extfield4\":\"\",\"extfield6\":\"\",\"extfield8\":\"\",\"businesscode\":\"IDB_DEVICE_MAINTAIN\",\"isleaf\":\"Y\",\"treecode\":\"00010003\",\"sortorder\":2,\"parentbusinessid\":\"0000067\",\"businessname\":\"计划性检修\",\"modifytime\":1510286772000,\"businessid\":\"0000070\",\"isvalid\":\"Y\",\"fullcode\":\"jihuaxingjianxiu\",\"shortcode\":\"jhxjx\"},{\"isbusinessprocess\":\"Y\",\"isorgviewflag\":\"Y\",\"businessproperty\":\"2\",\"businessdesc\":\"\",\"extfield2\":\"\",\"extfield3\":\"\",\"extfield7\":\"\",\"extfield1\":\"\",\"extfield5\":\"\",\"extfield4\":\"\",\"extfield6\":\"\",\"extfield8\":\"\",\"businesscode\":\"IDB_MATERIEL_DISTRIBUTE\",\"isleaf\":\"Y\",\"treecode\":\"00010004\",\"sortorder\":3,\"parentbusinessid\":\"0000067\",\"businessname\":\"物资配送\",\"modifytime\":1510373172000,\"businessid\":\"0000071\",\"isvalid\":\"Y\",\"fullcode\":\"wuzipeisong\",\"shortcode\":\"wzps\"},{\"isbusinessprocess\":\"Y\",\"isorgviewflag\":\"Y\",\"businessproperty\":\"2\",\"businessdesc\":\"\",\"extfield2\":\"\",\"extfield3\":\"\",\"extfield7\":\"\",\"extfield1\":\"\",\"extfield5\":\"\",\"extfield4\":\"\",\"extfield6\":\"\",\"extfield8\":\"\",\"businesscode\":\"IDB_ATTECHMENT_USE\",\"isleaf\":\"Y\",\"treecode\":\"00010005\",\"sortorder\":4,\"parentbusinessid\":\"0000067\",\"businessname\":\"备品备件领用\",\"modifytime\":1510459572000,\"businessid\":\"0000072\",\"isvalid\":\"Y\",\"fullcode\":\"beipinbeijianlingyong\",\"shortcode\":\"bpbjly\"}]";

        Gson gson = new Gson();
        List<Map<String, Object>> bsbList = gson.fromJson(str, new TypeToken<List<Map<String, Object>>>(){}.getType());
        List<Map<String, Object>> bsbListNew = recursion("", bsbList);
        System.out.println(bsbListNew);
    }

    List<Map<String, Object>> recursion2(String parentbusinessid,List<Map<String, Object>> list){
        List<Map<String, Object>> bsbListNew = new ArrayList<Map<String, Object>>();
        Iterator it = list.iterator();
        while (it.hasNext()){
            Map<String, Object> map = (Map<String, Object>)it.next();
            if (map.get("parentbusinessid").equals(parentbusinessid)){
                bsbListNew.add(map);
                it.remove();
            }
        }
        for (Map<String, Object> e:bsbListNew){
            e.put("children",recursion2(StringUtilsLang3.objectToString(e.get("businessid")), list));
        }

        return bsbListNew;
    }
}
