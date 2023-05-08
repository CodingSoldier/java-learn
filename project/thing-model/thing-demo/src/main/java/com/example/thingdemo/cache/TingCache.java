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
public class TingCache implements Serializable {

    private static final long serialVersionUID = 1L;

    private TingProfileCache profile;

    private List<TingPropertiesCache> properties;

    private List<TingEventCache> events;

    private List<TingActionCache> actions;

}


