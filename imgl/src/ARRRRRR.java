import java.util.ArrayList;
import java.util.List;


public class ARRRRRR {
	public static void main(String[] args){
		List<String> imgList=new ArrayList<String>();
		imgList.add("a");
		imgList.add("a");
		imgList.add("a");
		
		String[] imgNameArr=new String[]{"a","b","c"};
		for(int i=imgList.size()-1;i>(imgNameArr.length-1);i--){
			imgList.remove(i);
		}
		for(int i=imgList.size();i<imgNameArr.length;i++){
			imgList.add(imgNameArr[i]);
		}
		System.out.println(imgList);

	}
}
