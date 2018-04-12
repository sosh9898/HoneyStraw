# # HoneyStraw

![title](./image/splash_title.png)

부스트캠프 2기 안드로이드 과정에서 진행한 프로젝트입니다.

기존의 앱과는 다르게 에디터가 아닌 일반 사용자도 자신만의 꿀팁을 제작 가능합니다.

[**꿀빨대 소개 페이지**](https://sosh9898.github.io)

## # 주요 기능

* 꿀팁 제작 
  * 커버 사진과 배경 사진으로 기본 틀을 제작합니다.

  * 사진과 설명을 추가합니다.

  * 모든 사진은 자신이 원하는 방식으로 Crop 할 수 있습니다 - [**Ucrop Library**](https://github.com/Yalantis/uCrop)

    ````java
    //Ucrop Library 의 사용방법입니다.
    private void cropImage(String tempUri) {
          Uri mDestinationUri = Uri.fromFile(new File(getActivity().getCacheDir(), 	getImageNameToUri('image uri')));

            UCrop.Options options = new UCrop.Options();
            options.setToolbarColor(getResources().getColor(R.color.colorPrimary));
            options.setToolbarTitle("꿀팁 제조기");
            options.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));

            UCrop.of(Uri.parse(tempUri), mDestinationUri)
                    .withOptions(options)
                    .withAspectRatio(3.7f, 5.6f)
                    .withMaxResultSize(maxWidth, maxHeight)
                    .start(getActivity(), 'REQUEST_CODE');
        			//REQUEST_CODE를 설정하여 callback 을 구분합니다.
        }
    ````
* 꿀팁 보기
  * 다른 사용자가 올린 꿀팁을 확인 할 수 있습니다.
  * 꿀팁은 제작 방식에 따라 ViewPager가 수직, 수평 방식으로 보여집니다.
  * 각 팁은 조회 수와 댓글 수를 확인 할 수 있습니다.
  * 빨대하기를 눌러 빨대 탭에서 모아 볼 수 있습니다.
* 댓글 등록
  * 각 팁의 댓글을 등록 할 수 있습니다.
  * 좋아요를 많이 받은 베스트 댓글은 최상단에 표시됩니다.
* 알림 기능
  * 댓글 좋아요, 댓글 등록, 팁 빨대하기 등에 따라 제작자 혹은 댓글 작성자에게 알림이 전송됩니다.
  * 확인하지 않은 알림의 개수는 앱 아이콘에 표시됩니다.
* 검색 기능
  * 원하는 브랜드명, 제품 혹은 팁 제목을 검색 할 수 있습니다.


* 마이페이지
  * 자신의 프로필 상태와 사진을 수정 할 수 있습니다.
  * 자신이 올린 팁들을 한 눈에 확인 할 수 있습니다.






## # Issue & Troubleshooting

* **OOM**

  * Issue - 다수의 이미지를 서버에 업로드하는 과정에서 OOM 발생

  * Troubleshooting - Android Profiler 를 통해 메모리에 남아있는 bitmap을 확인

     사용이 끝난 bitmap 을 recycle 하여 GC의 대상이되도록 조정

    ````java
    //이미지를 업로드하는 함수로 일부 소스는 생략합니다.
    private void uploadImage(){
        	/*RequestBody 의 설정 ( 생략 )
        	서버로 보내는 자료는 대부분 문자열 형태이지만 다음과 같이 이미지의 전송이 필요한 경우
        	MultiPart의 활용으로 byte 전송이 가능합니다.
        	*/
    		BitmapFactory.Options options = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeStream(in, null, options);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);

            RequestBody photoBody = RequestBody.create(MediaType.parse("image/jpg"), baos.toByteArray());

            files[i] = new File(imgUrl.get(i));
            body[i] = MultipartBody.Part.createFormData("image", files[i].getName(), 		 photoBody);
        	//image 를 byte 형태로 전송하기위해 MultipartBody 형태로 변환합니다.
        
        	//Issue 의 원인으로 사용된 bitmap 을 다음과 같이 recycle 해주어 해결하였습니다.
            bitmap.recycle();
        
        	//Retrofit 을 활용한 서버와의 통신 ( 생략 )
     }
    ````

* **SparseArray**

  * 다음은 성능 개선을 위해 사용한 SparseArray 입니다.

  * SparseArray는 SparseArray 는 IntDef, StringDef가 Enum 을 대신하듯이, 안드로이드 내에서 성능 향상을 위해 제공되는 자료구조입니다. Java Collection Interface 를 지원하지 않는다는 단점이 있지만 다음의 장점을 제공합니다.

    * Integer 값을 키로하여 Objects 를 매핑
    * 각 인덱스 사이에 공간을 가질 수 있음
    * 인덱스에 데이터가 지워지면 deleted 로 표시하고 공간을 남김

    위 장점을 통해 메모리를 효율적으로 관리 할 수 있으나, 이 또한 적은 데이터에 한정됩니다.

  * 해당 프로젝트에서는 ViewPage 내의 Fragment 를 관리하는데 사용되었습니다.

    ````java
    // SparseArray<Fragment> sparseArray = new SparseArray<Fragment>();

    @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, 	position);
            sparseArray.put(position, fragment);
            return fragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            sparseArray.remove(position);
            super.destroyItem(container, position, object);
        }

    // 페이지 어댑터 내부입니다. Fragment 가 init 되는 순간 그리고 destroy 되는 순간에
    // 호출되는 메소느 내에서 position(integer)를 key 로 하여 Fragment(Value) 추가하고 제거합니다. 이렇게 구성된 SparseArray 로 각 Fragment 에 접근 할 수 있습니다.
    ````

* **Custom View**

  * TempleteView( TipView ) 라는 Custom View 를 만들어 레이아웃을 여러개 구성하지 않고, 하나의 뷰로 원하는 방식으로 재배치하는 CustomView 를 만들었습니다.

    ````java
    // 다음은 Custom View 내의 함수로 textview 와 imageview 의 위치를 번호에 맞춰 재배치합니다.
    private void setTempleate(int num){
            switch (num){
                case 1:
                paramsText= new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, 	
                convertDpToPx(200, getContext()));

                paramsText.addRule(RelativeLayout.ABOVE, R.id.tip_image);
                tipText.setLayoutParams(paramsText);
                break;
                //이하 생략
                default:
                   break;
            }
        }
    ````

    ​








