package com.margub.truecaller.model.common;


import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import com.margub.truecaller.Constants;
import orestes.bloomfilter.CountingBloomFilter;
import orestes.bloomfilter.FilterBuilder;

import java.nio.charset.StandardCharsets;


public class GlobalSpam {

    public static final GlobalSpam INSTANCE = new GlobalSpam();
    @SuppressWarnings("UnstableApiUsage")
    private BloomFilter<String> globalBlocked =
            BloomFilter.create(Funnels.stringFunnel(StandardCharsets.UTF_8),
                    Constants.MAX_GLOBAL_SPAM_COUNT);
    private CountingBloomFilter<String> globalSpam =
            new FilterBuilder(Constants.MAX_GLOBAL_SPAM_COUNT, 0.1)
                    .buildCountingBloomFilter();

    private GlobalSpam() {

    }

    public void reportSpam(String candidateSpamNumber, String reportingNumber, String reason) {
        System.out.println("Send metrics here for spam number: " + candidateSpamNumber +
                " reported " + reportingNumber + " for reason: " + reason);

        if (globalSpam.getEstimatedCount(candidateSpamNumber) >= Constants.MAX_COUNT_TO_MARK_GLOBAL_BLOCKED) {
            globalBlocked.put(candidateSpamNumber);
        } else {
            globalSpam.add(candidateSpamNumber);
        }
    }

    public boolean isGlobalSpam(String number) {
        return globalSpam.contains(number);
    }

    public boolean isGloballyBlocked(String number) {
        return globalBlocked.mightContain(number);
    }
}
