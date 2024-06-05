package MecerreyesExtraBack.MeceExtraBack;

import com.fasterxml.jackson.annotation.JsonAnySetter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatosMsCode {
        private Map<String, List<MsCode>> data = new HashMap<>();

        @JsonAnySetter
        public void setData(String key, List<MsCode> value) {
            this.data.put(key, value);
        }

        public Map<String, List<MsCode>> getData() {
            return data;
        }

        public void setData(Map<String, List<MsCode>> data) {
            this.data = data;
        }

        @Override
        public String toString() {
            return "DatosMSCODE{" +
                    "data=" + data +
                    '}';
        }
    }

