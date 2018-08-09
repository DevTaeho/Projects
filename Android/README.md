# 안드로이드 프로젝트입니다.

문화 데이터를 이용한 관광 애플리케이션입니다.

(작성중)

# Upadet on 2018-08-09
	- FirstFragment에서 관광지를 불러올 때, 텍스트는 있고 이미지는 없어도 계속 업데이트를 하는 문제가 있었음
		- Image를 Parsing 하다가 Text, ContentId가 있던말던 image URL이 나타나지 않으면 업데이트를 멈추게끔 수정
		- <!> 여기서도 문제점이 있음 -> 맨 마지막 CardView에서 스크롤링이 굉장히 느림
		
	- AttractionInDetail class 내에 ViewPager에 image를 URL을 통해 넣는 과정을 수정
		- 기존에는 API를 통해 Image URL을 String 배열에 저장
		  String[] image = new String[20] 와 같이 정적 배열을 생성해 할당
		- 수정 후엔 List<String> 리스트 오브젝트를 만들어서 Image URL이 생성될 때마다 List에 add 하는 동적인 방식으로 변경됨
		  List<String>에 저장된 Image URL의 수만큼 String 배열에 할당 (makeDynamicImageUrl 메소드를 만들어 구현)
		  
	- <!> 해당 관광지의 ContentId를 통해 관광지의 자세한 내용을 Parsing해야 되는데 결과가 나타나지 않음

		  
		  
Members : Dong Wook Lee, Tae ho Kim, Hye Jeong Moon

Write on : 2018-08-07