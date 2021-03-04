package com.SHAudit.singHealthAudit.Audit;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.util.Calendar;

public class CustomAuditorEntryDeserializer extends StdDeserializer<AuditorEntry> {
    public CustomAuditorEntryDeserializer(){
        this(null);
    }

    public CustomAuditorEntryDeserializer(Class<?> vc){
        super(vc);
    }

    @Override
    public AuditorEntry deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        AuditorEntry auditorEntry = new AuditorEntry();
        ObjectCodec codec = jsonParser.getCodec();
        JsonNode node = codec.readTree(jsonParser);

        //internal housekeeping
        auditorEntry.setEntry_id(-1); //idk
        auditorEntry.setDate(new Date(Calendar.getInstance().getTime().getTime()));
        auditorEntry.setTime(new Time(Calendar.getInstance().getTime().getTime()));

        //from json
        JsonNode qn_id_node = node.get("qn_id");
        auditorEntry.setQn_id(qn_id_node.asInt());
        //check pass or fail
        JsonNode passFailNode = node.get("passFail");
        auditorEntry.setStatus(passFailNode.asBoolean());
        //check if should store remarks and evidence
        if(!auditorEntry.getStatus()){
            JsonNode remarksNode = node.get("remarks");
            auditorEntry.setRemarks(remarksNode.asText());
            auditorEntry.setSeverity(node.get("severity").asInt());
        }

        return auditorEntry;
    }
}

