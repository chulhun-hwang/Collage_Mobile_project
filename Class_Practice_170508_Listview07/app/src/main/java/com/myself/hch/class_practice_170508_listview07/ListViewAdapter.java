package com.myself.hch.class_practice_170508_listview07;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/* 커스텀 어댑터 생성 - BaseAdapter
   - 어댑터가 필터링 기능을 지원하기 위해서는 Filterable 인터페이스를 구현하고,
     getFilter() 메소드를 Override하여 구현해야 함 */
public class ListViewAdapter extends BaseAdapter implements Filterable {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList. (원본 데이터 리스트)
   //(1) =====================
    private ArrayList<ListViewItem> listViewItemList = new ArrayList<ListViewItem>();
    // 필터링된 결과 데이터를 저장하기 위한 ArrayList. 최초에는 전체 리스트 저장.
    //(2) =====================
    private ArrayList<ListViewItem> filteredItemList = listViewItemList;
    //필터 클래스 선언
    Filter listFilter;

    //어댑터 생성자
    public  ListViewAdapter() {  }

    // Adapter에 사용되는 데이터의 개수를 리턴
    @Override
    public int getCount() {
        return filteredItemList.size();
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Context 참조
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조
        //(3) =====================
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item, parent, false);
        }
        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조
        ImageView iconImageView = (ImageView) convertView.findViewById(R.id.imageView);
        TextView titleTextView = (TextView) convertView.findViewById(R.id.textView1);
        TextView descTextView = (TextView) convertView.findViewById(R.id.textView2);

        // Data Set(filteredItemList)에서 position에 위치한 데이터 참조
        //(4) =====================
        ListViewItem listViewItem = filteredItemList.get(position);
        // 아이템 내 각 위젯에 데이터 반영
        //(5) =====================
        iconImageView.setImageDrawable(listViewItem.getIcon());
        titleTextView.setText(listViewItem.getTitle());
        descTextView.setText(listViewItem.getDescription());
        //리스트뷰에 아이템 리턴
        return convertView;
    }//end of getView()

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴
    @Override
    public Object getItem(int position) {
        //(6) =====================
        return filteredItemList.get(position);
    }

    // 지정한 위치(position)에 있는 데이터 리턴
    @Override
    public long getItemId(int position) {
        return position;
    }

    // 아이템 데이터 추가를 위한
    public void addItem(Drawable icon, String title, String desc) {
        ListViewItem item = new ListViewItem();

        item.setIcon(icon);
        item.setTitle(title);
        item.setDescription(desc);

        listViewItemList.add(item);
    }

    //Filterable 인터페이스의 추상 메서드 - 필수 구현
   @Override
    public Filter getFilter() {
        if (listFilter == null) {
            //(7) =====================
            listFilter = new ListFilter();
        }
        return listFilter;
    }

    /* 필터링 기능을 구현하기 위해, Filter 클래스를 상속한 커스텀 Filter 클래스를 정의
        - performFiltering() 메서드와 publishResults() 메서드를 Override하여 구현
     */
    private  class ListFilter extends Filter {

        /* performFiltering() 메서드는 필터링을 수행하는 메서드
           - 필터링을 수행하는 루프를 구현한 다음, 필터링된 결과 리스트를 FilterResults에 담아서 리턴함
         */
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            //필터링 결과를 담을 FilterResults 객체 생성
            FilterResults results = new FilterResults();

            
             //(8) =====================
            if(constraint == null || constraint.length() == 0){
                results.values = listViewItemList;
                results.count = listViewItemList.size();
            }else{
                ArrayList<ListViewItem> itemList = new ArrayList<ListViewItem>();
                for(ListViewItem item : listViewItemList){
                    if(item.getTitle().toUpperCase().contains(constraint.toString().toUpperCase()) ||
                            item.getDescription().toUpperCase().contains(constraint.toString().toUpperCase())){
                        itemList.add(item);
                    }
                }
                results.values = itemList;
                results.count = itemList.size();
            }
            return results;
        }//end of performFiltering()

        /* performFiltering() 메서드에서 필터링된 결과를 UI에 갱신시키는 역할을 수행
           - 커스텀 Adapter를 통한 ListView 갱신 작업을 구현하면 됨
         */
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            //필터링된 데이터 리스트를 filteredItemList(ArrayList)에 저장
            filteredItemList = (ArrayList<ListViewItem>) results.values;

            //(9) =====================
            if(results.count > 0 ){
                notifyDataSetChanged();
            }else{
                notifyDataSetInvalidated();
            }

        }//end of publishResults()
    }//end of ListFilter 클래스
}//end of ListViewAdapter
