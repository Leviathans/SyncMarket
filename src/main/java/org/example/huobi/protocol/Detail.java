package org.example.huobi.protocol;

import java.math.BigDecimal;

public class Detail {

        private Long amount;
        private Long ts;
        private Long id;
        private BigDecimal price;
        private String direction;
        private BigDecimal quantity;
        private BigDecimal tradeTurnover;

        public Long getAmount() {
            return amount;
        }

        public void setAmount(Long amount) {
            this.amount = amount;
        }

        public Long getTs() {
            return ts;
        }

        public void setTs(Long ts) {
            this.ts = ts;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }

        public String getDirection() {
            return direction;
        }

        public void setDirection(String direction) {
            this.direction = direction;
        }

        public BigDecimal getQuantity() {
            return quantity;
        }

        public void setQuantity(BigDecimal quantity) {
            this.quantity = quantity;
        }

        public BigDecimal getTradeTurnover() {
            return tradeTurnover;
        }

        public void setTradeTurnover(BigDecimal tradeTurnover) {
            this.tradeTurnover = tradeTurnover;
        }
}
