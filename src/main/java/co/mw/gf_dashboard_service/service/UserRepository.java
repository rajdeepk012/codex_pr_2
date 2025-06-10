package co.mw.gf_dashboard_service.service;

import co.mw.gf_dashboard_service.model.CaseCounts;
import co.mw.gf_dashboard_service.model.UserDetails;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserRepository extends MongoRepository<UserDetails, String> {

    @Aggregation(pipeline = {
            "{$lookup: {from: 'field_executive_master_data', localField: 'user.mobile', foreignField: 'mobile', as: 'aggregated_data'}}",
            "{ $addFields: { 'user.functionalArea': {$arrayElemAt: ['$aggregated_data.functionalArea', 0]}, 'user.pincode': {$arrayElemAt: ['$aggregated_data.pincode', 0]}, 'user.city': {$arrayElemAt: ['$aggregated_data.city', 0]}, 'user.employeeCode': {$arrayElemAt: ['$aggregated_data.employeeCode', 0]}}}",
            "{ $project: { _id: 0, 'aggregated_data': 0, }}"
    })
    Slice<UserDetails> getAllUsers(Pageable pageable);

    @Aggregation(pipeline = {
            "{$lookup: {from: 'field_executive_master_data', localField: 'user.mobile', foreignField: 'mobile', as: 'aggregated_data'}}",
            "{ $addFields: { 'user.functionalArea': {$arrayElemAt: ['$aggregated_data.functionalArea', 0]}, 'user.pincode': {$arrayElemAt: ['$aggregated_data.pincode', 0]}, 'user.city': {$arrayElemAt: ['$aggregated_data.city', 0]}, 'user.employeeCode': {$arrayElemAt: ['$aggregated_data.employeeCode', 0]}}}",
            "{ $match : { 'aggregated_data.pincode' : ?0 } }",
            "{ $project: { _id: 0, 'aggregated_data': 0, }}"
    })
    Slice<UserDetails> getAllUsersByPincode(Pageable pageable,String pincode);

    @Aggregation(pipeline = {
            "{ $group: { _id: 'caseCounts', assignedCaseCount: { $sum: '$caseInfo.assignedCaseCount' }, draftCaseCount: { $sum: '$caseInfo.draftCaseCount' }}}"
    })
    CaseCounts getAssignAndDraftCaseCounts();

    @Aggregation(pipeline = {
            "{ $unwind: '$caseInfo.cases' }",
            "{ $match: { $expr: { $eq: [ { $arrayElemAt: [ { $split: ['$caseInfo.cases.caseCompletionAt', 'T'] }, 0 ] }, { $dateToString: { date: new Date(), format: '%Y-%m-%d'} } ]}}}",
            "{ $count: 'completedCaseCount' }"
    })
    List<Integer> getCompletedCaseCounts();

    @Aggregation(pipeline = {
            "{$lookup: {from: 'field_executive_master_data', localField: 'user.mobile', foreignField: 'mobile', as: 'aggregated_data'}}",
            "{ $addFields: { 'user.functionalArea': {$arrayElemAt: ['$aggregated_data.functionalArea', 0]}, 'user.pincode': {$arrayElemAt: ['$aggregated_data.pincode', 0]}, 'user.city': {$arrayElemAt: ['$aggregated_data.city', 0]}}}",
            "{ $match : { 'aggregated_data.pincode' : ?0 } }",
            "{ $group: { _id: 'caseCounts', assignedCaseCount: { $sum: '$caseInfo.assignedCaseCount' }, draftCaseCount: { $sum: '$caseInfo.draftCaseCount' }}}",
            "{ $project: { _id: 0, 'aggregated_data': 0, user: 0, deviceInfo: 0, caseInfo: 0}} "
    })
    CaseCounts getAssignAndDraftCaseCountsByPincode(String pincode);

    @Aggregation(pipeline = {
            "{$lookup: {from: 'field_executive_master_data', localField: 'user.mobile', foreignField: 'mobile', as: 'aggregated_data'}}",
            "{ $addFields: { 'user.functionalArea': {$arrayElemAt: ['$aggregated_data.functionalArea', 0]}, 'user.pincode': {$arrayElemAt: ['$aggregated_data.pincode', 0]}, 'user.city': {$arrayElemAt: ['$aggregated_data.city', 0]}}}",
            "{ $match : { 'aggregated_data.pincode' : ?0 } }",
            "{ $unwind: '$caseInfo.cases' }",
            "{ $match: { $expr: { $eq: [ { $arrayElemAt: [ { $split: ['$caseInfo.cases.caseCompletionAt', 'T'] }, 0 ] }, { $dateToString: { date: new Date(), format: '%Y-%m-%d'} } ]}}}",
            "{ $count: 'completedCaseCount' }"
    })
    List<Integer> getCompletedCaseCountsByPincode(String pincode);
}
