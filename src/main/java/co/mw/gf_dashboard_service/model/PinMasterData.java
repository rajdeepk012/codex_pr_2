package co.mw.gf_dashboard_service.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PinMasterData {

    public void setRecords(List<PinRecord> records) {
        this.records = records;
    }

    private List<PinRecord> records;

    public List<PinRecord> getRecords() {
        return records;
    }
}
