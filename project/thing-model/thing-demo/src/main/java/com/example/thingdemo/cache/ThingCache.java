package com.example.thingdemo.cache;

import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author chenpq
 * @since 2023-05-04 14:08:27
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ThingCache implements Serializable {

    private static final long serialVersionUID = 1L;

    private ThingProfileCache profile;

    private List<ThingPropertiesCache> properties;

    private List<ThingEventCache> events;

    private List<ThingActionCache> actions;

}


