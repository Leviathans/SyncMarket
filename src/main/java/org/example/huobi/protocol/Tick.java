package org.example.huobi.protocol;

import java.util.ArrayList;
import java.util.List;

public class Tick {

        private String id;
        private Long ts;
        private List<Detail> data = new ArrayList<>();

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Long getTs() {
            return ts;
        }

        public void setTs(Long ts) {
            this.ts = ts;
        }

        public List<Detail> getData() {
            return data;
        }

        public void setData(List<Detail> data) {
            this.data = data;
        }
}
