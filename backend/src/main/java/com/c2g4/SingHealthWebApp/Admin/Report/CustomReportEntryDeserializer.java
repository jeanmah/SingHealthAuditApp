package com.c2g4.SingHealthWebApp.Admin.Report;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.util.Calendar;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class CustomReportEntryDeserializer extends StdDeserializer<ReportEntry> {
    public CustomReportEntryDeserializer(){
        this(null);
    }

    public CustomReportEntryDeserializer(Class<?> vc){
        super(vc);
    }

    @Override
    public ReportEntry deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) 
    		throws IOException, JsonProcessingException {
        ReportEntry entry = new ReportEntry();
        ObjectCodec codec = jsonParser.getCodec();
        JsonNode node = codec.readTree(jsonParser);

        //internal housekeeping
        entry.setEntry_id(-1); //idk
        entry.setDate(new Date(Calendar.getInstance().getTime().getTime()));
        entry.setTime(new Time(Calendar.getInstance().getTime().getTime()));

        //from json
        JsonNode qn_id_node = node.get("qn_id");
        entry.setQn_id(qn_id_node.asInt());
        //check pass or fail
        JsonNode passFailNode = node.get("passFail");
        entry.setStatus(passFailNode.asBoolean());
        //check if should store remarks and evidence
        if(entry.getStatus() == Component_Status.FAIL){
            JsonNode remarksNode = node.get("remarks");
            entry.setRemarks(remarksNode.asText());
            entry.setSeverity(node.get("severity").asInt());
        }

        return entry;
    }
}

