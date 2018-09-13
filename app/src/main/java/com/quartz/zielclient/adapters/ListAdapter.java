package com.quartz.zielclient.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.quartz.zielclient.R;
import com.quartz.zielclient.models.ListItem;

import java.util.List;

/**
 * Adapter class for adapting data to integrate them into the ListViews
 *
 * @author wei how ng
 */
public class ListAdapter extends RecyclerView.Adapter<ListAdapter.TextViewHolder> {

  private List<ListItem> listItems;
  private Context context;

  // Constructor
  public ListAdapter(List<ListItem> listItems, Context context) {
    this.listItems = listItems;
    this.context = context;
  }

  // Create ViewGroup whenever TextViewHolder gets instantiated
  @NonNull
  @Override
  public TextViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    // Get context of the view from respective XML
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.assisted_list_item, parent, false);

    // Return view
    return new TextViewHolder(view);
  }

  // statusing the data to the ViewHolders
  @Override
  public void onBindViewHolder(@NonNull TextViewHolder textViewHolder, int i) {
    ListItem listItem = listItems.get(i);

    // Fetching the Names and Descriptions
    textViewHolder.textViewName.setText(listItem.getName());
    textViewHolder.textViewDesc.setText(listItem.getDescription());
  }

  // Returns size of list
  @Override
  public int getItemCount() {
    return listItems.size();
  }

  /**
   * TextViewHolder class made for this ListAdapter
   */
  class TextViewHolder extends RecyclerView.ViewHolder {

    // Defining TextViews of the Assisted List Objects
    private TextView textViewName;
    private TextView textViewDesc;

    TextViewHolder(@NonNull View itemView) {
      super(itemView);

      textViewName = itemView.findViewById(R.id.assistedListItemName);
      textViewDesc = itemView.findViewById(R.id.assistedListItemDesc);
    }
  }
}
