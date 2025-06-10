package co.mw.gf_dashboard_service.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class FieldExecutionData {

    public void setRecords(List<Record> records) {
        this.records = records;
    }

    private List<Record> records;

    public List<Record> getRecords() {
        return records;
    }
}
