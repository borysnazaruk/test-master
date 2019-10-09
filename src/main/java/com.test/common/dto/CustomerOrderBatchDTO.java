package com.test.common.dto;

import lombok.Data;
import org.json.simple.JSONObject;

import java.io.Serializable;
import java.util.List;

@Data
public final class CustomerOrderBatchDTO implements Serializable {
    public String cutoff_time;
    public String id;
    public List<Item> items;
    public String location_id;

    public CustomerOrderBatchDTO(String cutoff_time, String id, List<Item> items, String location_id) {
        this.cutoff_time = cutoff_time;
        this.id = id;
        this.items = items;
        this.location_id = location_id;
    }

    public CustomerOrderBatchDTO() {

    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    @Data
    public static final class Item {
        public String id;
        public int qty;

        public Item() {
        }

        public Item(String id, int qty){
            this.id = id;
            this.qty = qty;
        }
    }
}