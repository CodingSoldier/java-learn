package com.thinkinginjava;

import java.util.*;

import static java.util.Collections.binarySearch;
import static org.eclipse.osgi.internal.debug.Debug.print;

/**
 * @Author：陈丕迁
 * @Description：
 * @Date： 2018-04-20
 */
public class L_Array {
}


class BerylliumSphere{}
class E01_ArrayInitialization {
    static void hide(BerylliumSphere[] s) {
        System.out.println("Hiding " + s.length + " sphere(s)");
    }
    public static void main(String[] args) {
// Dynamic aggregate initialization:
        hide(new BerylliumSphere[]{ new BerylliumSphere(),
                new BerylliumSphere() });
// The following line produces a compilation error.
// hide({ new BerylliumSphere() });
// Aggregate initialization:
        BerylliumSphere[] d = { new BerylliumSphere(),
                new BerylliumSphere(), new BerylliumSphere() };
        hide(d);
// Dynamic aggregate initialization is redundant
// in the next case:
        BerylliumSphere[] a = new BerylliumSphere[]{
                new BerylliumSphere(), new BerylliumSphere() };
        hide(a);
    }
}





// P450
class E11_AutoboxingWithArrays {
    public static void main(String[] args) {
        int[] pa = { 1, 2, 3, 4, 5 };
        //Integer[] wa = pa;
        Integer[] wb = { 1, 2, 3, 4, 5 };
        //int[] pb = wb;
        //自动包装机制不能应用于数组

        Arrays.sort(wb, Collections.reverseOrder());
    }
}


// Countries国家类
class Countries {
    public static final String[][] DATA = {
            // Africa
            {"ALGERIA","Algiers"}, {"ANGOLA","Luanda"},
            {"BENIN","Porto-Novo"}, {"BOTSWANA","Gaberone"},
            {"BURKINA FASO","Ouagadougou"},
            {"BURUNDI","Bujumbura"},
            {"CAMEROON","Yaounde"}, {"CAPE VERDE","Praia"},
            {"CENTRAL AFRICAN REPUBLIC","Bangui"},
            {"CHAD","N'djamena"},  {"COMOROS","Moroni"},
            {"CONGO","Brazzaville"}, {"DJIBOUTI","Dijibouti"},
            {"EGYPT","Cairo"}, {"EQUATORIAL GUINEA","Malabo"},
            {"ERITREA","Asmara"}, {"ETHIOPIA","Addis Ababa"},
            {"GABON","Libreville"}, {"THE GAMBIA","Banjul"},
            {"GHANA","Accra"}, {"GUINEA","Conakry"},
            {"BISSAU","Bissau"},
            {"COTE D'IVOIR (IVORY COAST)","Yamoussoukro"},
            {"KENYA","Nairobi"}, {"LESOTHO","Maseru"},
            {"LIBERIA","Monrovia"}, {"LIBYA","Tripoli"},
            {"MADAGASCAR","Antananarivo"}, {"MALAWI","Lilongwe"},
            {"MALI","Bamako"}, {"MAURITANIA","Nouakchott"},
            {"MAURITIUS","Port Louis"}, {"MOROCCO","Rabat"},
            {"MOZAMBIQUE","Maputo"}, {"NAMIBIA","Windhoek"},
            {"NIGER","Niamey"}, {"NIGERIA","Abuja"},
            {"RWANDA","Kigali"},
            {"SAO TOME E PRINCIPE","Sao Tome"},
            {"SENEGAL","Dakar"}, {"SEYCHELLES","Victoria"},
            {"SIERRA LEONE","Freetown"}, {"SOMALIA","Mogadishu"},
            {"SOUTH AFRICA","Pretoria/Cape Town"},
            {"SUDAN","Khartoum"},
            {"SWAZILAND","Mbabane"}, {"TANZANIA","Dodoma"},
            {"TOGO","Lome"}, {"TUNISIA","Tunis"},
            {"UGANDA","Kampala"},
            {"DEMOCRATIC REPUBLIC OF THE CONGO (ZAIRE)",
                    "Kinshasa"},
            {"ZAMBIA","Lusaka"}, {"ZIMBABWE","Harare"},
            // Asia
            {"AFGHANISTAN","Kabul"}, {"BAHRAIN","Manama"},
            {"BANGLADESH","Dhaka"}, {"BHUTAN","Thimphu"},
            {"BRUNEI","Bandar Seri Begawan"},
            {"CAMBODIA","Phnom Penh"},
            {"CHINA","Beijing"}, {"CYPRUS","Nicosia"},
            {"INDIA","New Delhi"}, {"INDONESIA","Jakarta"},
            {"IRAN","Tehran"}, {"IRAQ","Baghdad"},
            {"ISRAEL","Jerusalem"}, {"JAPAN","Tokyo"},
            {"JORDAN","Amman"}, {"KUWAIT","Kuwait City"},
            {"LAOS","Vientiane"}, {"LEBANON","Beirut"},
            {"MALAYSIA","Kuala Lumpur"}, {"THE MALDIVES","Male"},
            {"MONGOLIA","Ulan Bator"},
            {"MYANMAR (BURMA)","Rangoon"},
            {"NEPAL","Katmandu"}, {"NORTH KOREA","P'yongyang"},
            {"OMAN","Muscat"}, {"PAKISTAN","Islamabad"},
            {"PHILIPPINES","Manila"}, {"QATAR","Doha"},
            {"SAUDI ARABIA","Riyadh"}, {"SINGAPORE","Singapore"},
            {"SOUTH KOREA","Seoul"}, {"SRI LANKA","Colombo"},
            {"SYRIA","Damascus"},
            {"TAIWAN (REPUBLIC OF CHINA)","Taipei"},
            {"THAILAND","Bangkok"}, {"TURKEY","Ankara"},
            {"UNITED ARAB EMIRATES","Abu Dhabi"},
            {"VIETNAM","Hanoi"}, {"YEMEN","Sana'a"},
            // Australia and Oceania
            {"AUSTRALIA","Canberra"}, {"FIJI","Suva"},
            {"KIRIBATI","Bairiki"},
            {"MARSHALL ISLANDS","Dalap-Uliga-Darrit"},
            {"MICRONESIA","Palikir"}, {"NAURU","Yaren"},
            {"NEW ZEALAND","Wellington"}, {"PALAU","Koror"},
            {"PAPUA NEW GUINEA","Port Moresby"},
            {"SOLOMON ISLANDS","Honaira"}, {"TONGA","Nuku'alofa"},
            {"TUVALU","Fongafale"}, {"VANUATU","< Port-Vila"},
            {"WESTERN SAMOA","Apia"},
            // Eastern Europe and former USSR
            {"ARMENIA","Yerevan"}, {"AZERBAIJAN","Baku"},
            {"BELARUS (BYELORUSSIA)","Minsk"},
            {"BULGARIA","Sofia"}, {"GEORGIA","Tbilisi"},
            {"KAZAKSTAN","Almaty"}, {"KYRGYZSTAN","Alma-Ata"},
            {"MOLDOVA","Chisinau"}, {"RUSSIA","Moscow"},
            {"TAJIKISTAN","Dushanbe"}, {"TURKMENISTAN","Ashkabad"},
            {"UKRAINE","Kyiv"}, {"UZBEKISTAN","Tashkent"},
            // Europe
            {"ALBANIA","Tirana"}, {"ANDORRA","Andorra la Vella"},
            {"AUSTRIA","Vienna"}, {"BELGIUM","Brussels"},
            {"BOSNIA","-"}, {"HERZEGOVINA","Sarajevo"},
            {"CROATIA","Zagreb"}, {"CZECH REPUBLIC","Prague"},
            {"DENMARK","Copenhagen"}, {"ESTONIA","Tallinn"},
            {"FINLAND","Helsinki"}, {"FRANCE","Paris"},
            {"GERMANY","Berlin"}, {"GREECE","Athens"},
            {"HUNGARY","Budapest"}, {"ICELAND","Reykjavik"},
            {"IRELAND","Dublin"}, {"ITALY","Rome"},
            {"LATVIA","Riga"}, {"LIECHTENSTEIN","Vaduz"},
            {"LITHUANIA","Vilnius"}, {"LUXEMBOURG","Luxembourg"},
            {"MACEDONIA","Skopje"}, {"MALTA","Valletta"},
            {"MONACO","Monaco"}, {"MONTENEGRO","Podgorica"},
            {"THE NETHERLANDS","Amsterdam"}, {"NORWAY","Oslo"},
            {"POLAND","Warsaw"}, {"PORTUGAL","Lisbon"},
            {"ROMANIA","Bucharest"}, {"SAN MARINO","San Marino"},
            {"SERBIA","Belgrade"}, {"SLOVAKIA","Bratislava"},
            {"SLOVENIA","Ljuijana"}, {"SPAIN","Madrid"},
            {"SWEDEN","Stockholm"}, {"SWITZERLAND","Berne"},
            {"UNITED KINGDOM","London"}, {"VATICAN CITY","---"},
            // North and Central America
            {"ANTIGUA AND BARBUDA","Saint John's"},
            {"BAHAMAS","Nassau"},
            {"BARBADOS","Bridgetown"}, {"BELIZE","Belmopan"},
            {"CANADA","Ottawa"}, {"COSTA RICA","San Jose"},
            {"CUBA","Havana"}, {"DOMINICA","Roseau"},
            {"DOMINICAN REPUBLIC","Santo Domingo"},
            {"EL SALVADOR","San Salvador"},
            {"GRENADA","Saint George's"},
            {"GUATEMALA","Guatemala City"},
            {"HAITI","Port-au-Prince"},
            {"HONDURAS","Tegucigalpa"}, {"JAMAICA","Kingston"},
            {"MEXICO","Mexico City"}, {"NICARAGUA","Managua"},
            {"PANAMA","Panama City"}, {"ST. KITTS","-"},
            {"NEVIS","Basseterre"}, {"ST. LUCIA","Castries"},
            {"ST. VINCENT AND THE GRENADINES","Kingstown"},
            {"UNITED STATES OF AMERICA","Washington, D.C."},
            // South America
            {"ARGENTINA","Buenos Aires"},
            {"BOLIVIA","Sucre (legal)/La Paz(administrative)"},
            {"BRAZIL","Brasilia"}, {"CHILE","Santiago"},
            {"COLOMBIA","Bogota"}, {"ECUADOR","Quito"},
            {"GUYANA","Georgetown"}, {"PARAGUAY","Asuncion"},
            {"PERU","Lima"}, {"SURINAME","Paramaribo"},
            {"TRINIDAD AND TOBAGO","Port of Spain"},
            {"URUGUAY","Montevideo"}, {"VENEZUELA","Caracas"},
    };
    // Use AbstractMap by implementing entrySet()
    private static class FlyweightMap
            extends AbstractMap<String,String> {
        private static class Entry
                implements Map.Entry<String,String> {
            int index;
            Entry(int index) { this.index = index; }
            public boolean equals(Object o) {
                return DATA[index][0].equals(o);
            }
            public String getKey() { return DATA[index][0]; }
            public String getValue() { return DATA[index][1]; }
            public String setValue(String value) {
                throw new UnsupportedOperationException();
            }
            public int hashCode() {
                return DATA[index][0].hashCode();
            }
        }
        // Use AbstractSet by implementing size() & iterator()
        static class EntrySet
                extends AbstractSet<Map.Entry<String,String>> {
            private int size;
            EntrySet(int size) {
                if(size < 0)
                    this.size = 0;
                    // Can't be any bigger than the array:
                else if(size > DATA.length)
                    this.size = DATA.length;
                else
                    this.size = size;
            }
            public int size() { return size; }
            private class Iter
                    implements Iterator<Map.Entry<String,String>> {
                // Only one Entry object per Iterator:
                private Entry entry = new Entry(-1);
                public boolean hasNext() {
                    return entry.index < size - 1;
                }
                public Map.Entry<String,String> next() {
                    entry.index++;
                    return entry;
                }
                public void remove() {
                    throw new UnsupportedOperationException();
                }
            }
            public
            Iterator<Map.Entry<String,String>> iterator() {
                return new Iter();
            }
        }
        private static Set<Map.Entry<String,String>> entries =
                new EntrySet(DATA.length);
        public Set<Map.Entry<String,String>> entrySet() {
            return entries;
        }
    }
    // Create a partial map of 'size' countries:
    static Map<String,String> select(final int size) {
        return new FlyweightMap() {
            public Set<Map.Entry<String,String>> entrySet() {
                return new EntrySet(size);
            }
        };
    }
    static Map<String,String> map = new FlyweightMap();
    public static Map<String,String> capitals() {
        return map; // The entire map
    }
    public static Map<String,String> capitals(int size) {
        return select(size); // A partial map
    }
    static List<String> names =
            new ArrayList<String>(map.keySet());
    // All the names:
    public static List<String> names() { return names; }
    // A partial list:
    public static List<String> names(int size) {
        return new ArrayList<String>(select(size).keySet());
    }
    public static void main(String[] args) {
        print(capitals(10));
        print(names(10));
        print(new HashMap<String,String>(capitals(3)));
        print(new LinkedHashMap<String,String>(capitals(3)));
        print(new TreeMap<String,String>(capitals(3)));
        print(new Hashtable<String,String>(capitals(3)));
        print(new HashSet<String>(names(6)));
        print(new LinkedHashSet<String>(names(6)));
        print(new TreeSet<String>(names(6)));
        print(new ArrayList<String>(names(6)));
        print(new LinkedList<String>(names(6)));
        print(capitals().get("BRAZIL"));
    }
}






//  P477
interface SListIterator<T> {
    boolean hasNext();
    T next();
    void remove();
    void add(T element);
}
class SList<T> {
    // Utilization of the 'Null Object' pattern
    private final Link<T> header = new Link<T>(null, null);
    private static class Link<T> {
        T element;
        Link<T> next;
        Link(T element, Link<T> next) {
            this.element = element;
            this.next = next;
        }
    }

    SList() { header.next = header; }
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append("[");
        for(SListIterator<T> it = iterator(); it.hasNext();) {
            T element = it.next();
            buf.append(element == this ? "(this SList)" :
                    String.valueOf(element));
            if(it.hasNext())
                buf.append(", ");
        }
        buf.append("]");
        return buf.toString();
    }

    public SListIterator<T> iterator() {
        return new SListIteratorImpl();
    }

    private class SListIteratorImpl
            implements SListIterator<T> {
        private Link<T> lastReturned = header;
        private Link<T> next;
        SListIteratorImpl() { next = header.next; }
        public boolean hasNext() { return next != header; }
        public T next() {
            if(next == header)
                throw new NoSuchElementException();
            lastReturned = next;
            next = next.next;
            return lastReturned.element;
        }
        public void remove() {
            if(lastReturned == header)
                throw new IllegalStateException();
// Find an element before the last returned one
            for(Link<T> curr = header; ; curr = curr.next)
                if(curr.next == lastReturned) {
                    curr.next = lastReturned.next;
                    break;
                }
            lastReturned = header;
        }
        public void add(T element) {
            lastReturned = header;
            Link<T> newLink = new Link<T>(element, next);
            if(header.next == header) // Empty list
                header.next = newLink;
            else {
// Find an element before the one pointed by 'next'
                for(Link<T> curr = header; ; curr = curr.next)
                    if(curr.next == next) {
                        curr.next = newLink;
                        break;
                    }
            }
        }
    }
}
class E08_SList {
    public static void main(String[] args) {
// First, show some use cases for SListIterator
        print("Demonstrating SListIterator...");
        SList<String> sl = new SList<String>();
        print(sl);
        SListIterator<String> slit = sl.iterator();
        slit.add("One");
        slit.add("Two");
        slit.add("Three");
        print(slit.hasNext());
        print(sl);
        slit = sl.iterator();
        slit.add("Four");
        for(; slit.hasNext();)
            print(slit.next());
        print(sl);
        slit = sl.iterator();
        print(slit.next());
        slit.remove();
        print(slit.next());
        print(sl);
// Now, show the same use cases for ListIterator, too
        print("\nDemonstrating ListIterator...");
        List<String> l = new ArrayList<String>();
        print(l);
        ListIterator<String> lit = l.listIterator();
        lit.add("One");
        lit.add("Two");
        lit.add("Three");
        print(lit.hasNext());
        print(l);
        lit = l.listIterator();
        lit.add("Four");
        for(; lit.hasNext();)
            print(lit.next());
        print(l);
        lit = l.listIterator();
        print(lit.next());
        lit.remove();
        print(lit.next());
        print(l);
    }
}











// P480
class CustomSortedSet<T> implements SortedSet<T> {
    private final List<T> list;
    public CustomSortedSet() { list = new LinkedList<T>(); }
    // If this sorted set is backed by an another one
    private CustomSortedSet(List<T> list) {
        this.list = list;
    }
    public String toString() { return list.toString(); }
    /*** Methods defined in the Collection interface ***/
    public int size() { return list.size(); }
    public boolean isEmpty() { return list.isEmpty(); }
    @SuppressWarnings("unchecked")
    public boolean contains(Object o) {
        checkForNull(o);
        return binarySearch((List<Comparable<T>>)list, (T)o) >= 0;
    }
    public Iterator<T> iterator() { return list.iterator(); }
    public Object[] toArray() { return list.toArray(); }
    public <T> T[] toArray(T[] a) { return list.toArray(a); }
    @SuppressWarnings("unchecked")
    public boolean add(T o) {
        checkForNull(o);
        int ip = binarySearch((List<Comparable<T>>)list, o);
        if(ip < 0) {
            ip = -(ip + 1);
            if(ip == list.size())
                list.add(o);
            else
                list.add(ip, o);
            return true;
        }
        return false;
    }
    public boolean remove(Object o) {
        checkForNull(o);
        return list.remove(o);
    }
    public boolean containsAll(Collection<?> c) {
        checkForNull(c);
        return list.containsAll(c);
    }
    public boolean addAll(Collection<? extends T> c) {
        checkForNull(c);
        checkForNullElements(c);
        boolean res = false;
        for(T item : c)
            res |= add(item);
        return res;
    }
    public boolean removeAll(Collection<?> c) {
        checkForNull(c);
        return list.removeAll(c);
    }
    public boolean retainAll(Collection<?> c) {
        checkForNull(c);
        return list.retainAll(c);
    }
    public void clear() { list.clear(); }
    public boolean equals(Object o) {
        return o instanceof CustomSortedSet &&
                list.equals(((CustomSortedSet<?>)o).list);
    }
    public int hashCode() { return list.hashCode(); }
    /*** Methods defined in the SortedSet interface ***/
    public Comparator<? super T> comparator() { return null; }
    public SortedSet<T> subSet(T fromElement, T toElement) {
        checkForNull(fromElement);
        checkForNull(toElement);
        int fromIndex = list.indexOf(fromElement);
        int toIndex = list.indexOf(toElement);
        checkForValidIndex(fromIndex);
        checkForValidIndex(toIndex);
        try {
            return new CustomSortedSet<T>(
                    list.subList(fromIndex, toIndex));
        } catch(IndexOutOfBoundsException e) {
            throw new IllegalArgumentException(e);
        }
    }
    public SortedSet<T> headSet(T toElement) {
        checkForNull(toElement);
        int toIndex = list.indexOf(toElement);
        checkForValidIndex(toIndex);
        try {
            return new CustomSortedSet<T>(
                    list.subList(0, toIndex));
        } catch(IndexOutOfBoundsException e) {
            throw new IllegalArgumentException(e);
        }
    }
    public SortedSet<T> tailSet(T fromElement) {
        checkForNull(fromElement);
        int fromIndex = list.indexOf(fromElement);
        checkForValidIndex(fromIndex);
        try {
            return new CustomSortedSet<T>(
                    list.subList(fromIndex, list.size()));
        } catch(IndexOutOfBoundsException e) {
            throw new IllegalArgumentException(e);
        }
    }
    public T first() {
        try {
            return list.get(0);
        } catch(IndexOutOfBoundsException e) {
            throw new NoSuchElementException();
        }
    }
    public T last() {
        try {
            return list.get(list.size() - 1);
        } catch(IndexOutOfBoundsException e) {
            throw new NoSuchElementException();
        }
    }
    /*** Utility methods ***/
    private void checkForNullElements(Collection<?> c) {
        for(Iterator<?> it = c.iterator(); it.hasNext();)
            if(it.next() == null)
                throw new NullPointerException();
    }
    private void checkForNull(Object o) {
        if(o == null)
            throw new NullPointerException();
    }
    private void checkForValidIndex(int idx) {
        if(idx == -1)
            throw new IllegalArgumentException();
    }
}
 class E10_CustomSortedSet {
    // The whole main() method is basically copy-pasted from
// containers/SortedSetDemo.java!
    public static void main(String[] args) {
        SortedSet<String> sortedSet =
                new CustomSortedSet<String>();
        Collections.addAll(sortedSet,
                "one two three four five six seven eight"
                        .split(" "));
        print(sortedSet);
        String low = sortedSet.first();
        String high = sortedSet.last();
        print(low);
        print(high);
        Iterator<String> it = sortedSet.iterator();
        for(int i = 0; i <= 6; i++) {
            if(i == 3) low = it.next();
            if(i == 6) high = it.next();
        }
        print(low);
        print(high);
        print(sortedSet.subSet(low, high));
        print(sortedSet.headSet(high));
        print(sortedSet.tailSet(low));
        print(sortedSet.contains("three"));
        print(sortedSet.contains("eleven"));
        print(sortedSet.addAll(Arrays.asList(
                "three", "eleven")));
        print(sortedSet);
        print(sortedSet.retainAll(Arrays.asList(
                "three", "eleven")));
        print(sortedSet);
// Demonstrate data integrity
        try {
            sortedSet.addAll(Arrays.asList("zero", null));
        } catch(NullPointerException e) {
            System.out.println("Null elements not supported!");
        }
// The set will not contain "zero"!
        print(sortedSet);
    }
}














// P483使用数组创建Map简单示例
class MapDemo<K, V>{
    private Object[][] pairs;
    private int index;

    public MapDemo(int length) {
        this.pairs = new Object[length][2];
    }
    public void put(K key, V val){
        if (index >= pairs.length){
            throw new RuntimeException();
        }
        pairs[index++] = new Object[]{key, val};
    }
    public V get(K key){
        for (int i=0; i<index; i++){
            if(key.equals(pairs[i][0])){
                return (V)pairs[i][1];
            }
        }
        return null;
    }

    public static void main(String[] args) {
        MapDemo<String, String> map = new MapDemo<String, String>(2);
        map.put("sky", "blue");
        map.put("grass", "green");
        try {
            map.put("extra", "object"); // Past the end
        } catch(RuntimeException e) {
            print("Too many objects!\n");
        }
        print(map.get("grass"));
    }
}


























