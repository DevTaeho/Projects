# 문제점

1. url로 이미지를 가져와서 프래그먼트에 이미지뷰에 넣는 경우
   다른 프래그먼트로 이동했다가 원래 프래그먼트로 왔을 시
   이미지가 소멸되는 문제점이 있음
   
   How to solve it ?
   
   Rest API로 이미지를 가져오고 가져온 Image Url을 Shared Preference에 저장하는 형태를 고안
   Shared Preference에 저장된 URL을 통해 이미지를 View에 넣어주는 방식을 생각중
   
2. 서버를 어떻게 할 것인가?

3. '좋아요' 한 오브젝트를 4번째 프래그먼트로 옮기기위한 방안을 생각해봐야함
   - SharedPreference에 '좋아요'한 내용에 해당하는 Title과 Image URL을 Shared Preference에 저장하는 쪽을 생각
